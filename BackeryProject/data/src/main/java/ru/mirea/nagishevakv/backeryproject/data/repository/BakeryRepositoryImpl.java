package ru.mirea.nagishevakv.backeryproject.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import ru.mirea.nagishevakv.backeryproject.data.local.dao.BakeryDao;
import ru.mirea.nagishevakv.backeryproject.data.local.db.BakeryDatabase;
import ru.mirea.nagishevakv.backeryproject.data.local.entity.OrderEntity;
import ru.mirea.nagishevakv.backeryproject.data.local.entity.ProductEntity;
import ru.mirea.nagishevakv.backeryproject.data.local.entity.UserEntity;
import ru.mirea.nagishevakv.backeryproject.data.network.NetworkApi;
import ru.mirea.nagishevakv.backeryproject.domain.model.Category;
import ru.mirea.nagishevakv.backeryproject.domain.model.Comment;
import ru.mirea.nagishevakv.backeryproject.domain.model.Order;
import ru.mirea.nagishevakv.backeryproject.domain.model.Product;
import ru.mirea.nagishevakv.backeryproject.domain.model.User;
import ru.mirea.nagishevakv.backeryproject.domain.repository.BakeryRepository;

public class BakeryRepositoryImpl implements BakeryRepository {
    private static final String TAG = "BakeryRepository";
    private static BakeryRepositoryImpl INSTANCE;
    private final BakeryDao bakeryDao;
    private final NetworkApi networkApi;
    private final SharedPreferences sharedPreferences;
    private FirebaseAuth firebaseAuth;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final MutableLiveData<Map<Product, Integer>> cartItems = new MutableLiveData<>(new HashMap<>());

    private BakeryRepositoryImpl(Context context) {
        this.bakeryDao = BakeryDatabase.getDatabase(context).bakeryDao();
        this.networkApi = new NetworkApi();
        this.sharedPreferences = context.getSharedPreferences("bakery_prefs", Context.MODE_PRIVATE);
        try {
            this.firebaseAuth = FirebaseAuth.getInstance();
        } catch (Exception e) {
            Log.e(TAG, "Firebase not initialized", e);
        }
    }

    public static synchronized BakeryRepositoryImpl getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new BakeryRepositoryImpl(context.getApplicationContext());
        }
        return INSTANCE;
    }

    @Override
    public LiveData<Boolean> login(String email, String password) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            result.setValue(false);
            return result;
        }

        if (firebaseAuth != null) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && firebaseAuth.getCurrentUser() != null) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        User user = new User(firebaseUser.getUid(), "User", firebaseUser.getEmail(), "", 0);
                        saveClientInfo(user);
                        result.setValue(true);
                    } else {
                        Log.e(TAG, "Firebase Login failed, falling back to local", task.getException());
                        performLocalAuth(email, email.split("@")[0], result);
                    }
                });
        } else {
            performLocalAuth(email, email.split("@")[0], result);
        }
        return result;
    }

    @Override
    public LiveData<Boolean> register(String email, String password, String nickname) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            result.setValue(false);
            return result;
        }

        if (firebaseAuth != null) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && firebaseAuth.getCurrentUser() != null) {
                        String uid = firebaseAuth.getCurrentUser().getUid();
                        User user = new User(uid, nickname, email, "", 0);
                        saveClientInfo(user);
                        result.setValue(true);
                    } else {
                        Log.e(TAG, "Firebase Registration failed, falling back to local", task.getException());
                        performLocalAuth(email, nickname, result);
                    }
                });
        } else {
            performLocalAuth(email, nickname, result);
        }
        return result;
    }

    private void performLocalAuth(String email, String nickname, MutableLiveData<Boolean> result) {
        String uid = "local_" + email.hashCode();
        User user = new User(uid, nickname, email, "", 0);
        saveClientInfo(user);
        result.setValue(true);
    }

    @Override
    public void logout() {
        if (firebaseAuth != null) firebaseAuth.signOut();
        sharedPreferences.edit().clear().apply();
        clearCart();
    }

    @Override
    public boolean isAuthorized() {
        String id = sharedPreferences.getString("user_id", "");
        return !id.isEmpty() && !id.startsWith("guest_");
    }

    @Override
    public LiveData<User> getClientInfo() {
        MutableLiveData<User> data = new MutableLiveData<>();
        String id = sharedPreferences.getString("user_id", "");
        String name = sharedPreferences.getString("user_name", "Гость");
        String email = sharedPreferences.getString("user_email", "");
        data.setValue(new User(id, name, email, "", 0));
        return data;
    }

    @Override
    public void saveClientInfo(User user) {
        sharedPreferences.edit()
                .putString("user_id", user.getId())
                .putString("user_name", user.getNickname())
                .putString("user_email", user.getEmail())
                .apply();
        if (!user.getId().startsWith("guest_")) {
            executor.execute(() -> {
                try {
                    bakeryDao.insertUser(new UserEntity(user.getId(), user.getNickname(), user.getEmail(), "", 0));
                } catch (Exception e) {
                    Log.e(TAG, "Error saving user to DB", e);
                }
            });
        }
    }

    @Override
    public LiveData<List<Product>> getProducts() {
        MediatorLiveData<List<Product>> mediator = new MediatorLiveData<>();
        LiveData<List<ProductEntity>> dbSource = bakeryDao.getAllProducts();
        LiveData<List<Product>> networkSource = networkApi.getProducts();

        mediator.addSource(dbSource, entities -> {
            if (entities != null && !entities.isEmpty()) {
                mediator.setValue(entities.stream()
                        .map(e -> new Product(e.id, e.name, e.categoryId, e.weightOrVolume, e.unit, e.description, e.price, e.imageUrl))
                        .collect(Collectors.toList()));
            }
        });

        mediator.addSource(networkSource, products -> {
            if (products != null) {
                mediator.setValue(products);
                executor.execute(() -> {
                    bakeryDao.insertProducts(products.stream()
                            .map(p -> new ProductEntity(p.getId(), p.getName(), p.getCategoryId(), p.getWeightOrVolume(), p.getUnit(), p.getDescription(), p.getPrice(), p.getImageUrl()))
                            .collect(Collectors.toList()));
                });
            }
        });
        return mediator;
    }

    @Override
    public LiveData<List<Category>> getCategories() { return networkApi.getCategories(); }
    @Override
    public LiveData<List<Comment>> getComments(int productId) { return new MutableLiveData<>(new ArrayList<>()); }
    
    @Override
    public LiveData<List<Order>> getOrders() {
        String userId = sharedPreferences.getString("user_id", "");
        MediatorLiveData<List<Order>> mediator = new MediatorLiveData<>();
        mediator.addSource(bakeryDao.getAllOrders(), entities -> {
            if (entities != null) {
                List<Order> filtered = entities.stream()
                        .filter(e -> e.userId.equals(userId))
                        .map(e -> new Order(e.id, e.userId, e.cost, e.itemCount, e.itemsDescription, e.date, e.city, e.status))
                        .collect(Collectors.toList());
                mediator.setValue(filtered);
            }
        });
        return mediator;
    }

    @Override
    public void addToCart(Product product) {
        Map<Product, Integer> current = new HashMap<>(cartItems.getValue());
        current.put(product, current.getOrDefault(product, 0) + 1);
        cartItems.setValue(current);
    }

    @Override
    public void removeFromCart(Product product) {
        Map<Product, Integer> current = new HashMap<>(cartItems.getValue());
        Product found = null;
        for (Product p : current.keySet()) {
            if (p.getId() == product.getId()) {
                found = p;
                break;
            }
        }
        if (found != null) {
            int count = current.get(found);
            if (count > 1) current.put(found, count - 1);
            else current.remove(found);
            cartItems.setValue(current);
        }
    }

    @Override
    public void clearCart() { cartItems.setValue(new HashMap<>()); }
    @Override
    public LiveData<Map<Product, Integer>> getCartItems() { return cartItems; }
    
    @Override
    public void createOrder(double cost, int itemCount, String itemsDescription, String city) {
        String userId = sharedPreferences.getString("user_id", "");
        String date = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(new Date());
        executor.execute(() -> {
            bakeryDao.insertOrder(new OrderEntity(userId, cost, itemCount, itemsDescription, date, city, "Активен"));
        });
    }
}