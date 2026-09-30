package ru.mirea.nagishevakv.backeryproject.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ru.mirea.nagishevakv.backeryproject.data.local.dao.BakeryDao;
import ru.mirea.nagishevakv.backeryproject.data.local.db.BakeryDatabase;
import ru.mirea.nagishevakv.backeryproject.data.local.entity.CategoryEntity;
import ru.mirea.nagishevakv.backeryproject.data.local.entity.CommentEntity;
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
    private static BakeryRepositoryImpl instance;

    private final BakeryDao bakeryDao;
    private final NetworkApi networkApi;
    private final SharedPreferences sharedPreferences;
    private final Executor executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<Map<Product, Integer>> cartItemsLiveData = new MutableLiveData<>(new HashMap<>());
    private final Map<Product, Integer> cartMap = new HashMap<>();

    private BakeryRepositoryImpl(Context context) {
        BakeryDatabase db = BakeryDatabase.getDatabase(context);
        this.bakeryDao = db.bakeryDao();
        this.networkApi = NetworkApi.getInstance();
        this.sharedPreferences = context.getSharedPreferences("bakery_prefs", Context.MODE_PRIVATE);
    }

    public static synchronized BakeryRepositoryImpl getInstance(Context context) {
        if (instance == null) {
            instance = new BakeryRepositoryImpl(context);
        }
        return instance;
    }

    @Override
    public LiveData<Boolean> login(String email, String password) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        if (email.contains("@") && password.length() >= 6) {
            sharedPreferences.edit()
                    .putString("client_email", email)
                    .putString("client_nickname", email.split("@")[0])
                    .putString("client_id", "uid_" + email.hashCode())
                    .apply();
            result.postValue(true);
        } else {
            result.postValue(false);
        }
        return result;
    }

    @Override
    public LiveData<Boolean> register(String email, String password, String nickname) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        if (email.contains("@") && password.length() >= 6 && !nickname.isEmpty()) {
            sharedPreferences.edit()
                    .putString("client_email", email)
                    .putString("client_nickname", nickname)
                    .putString("client_id", "uid_" + email.hashCode())
                    .apply();
            
            executor.execute(() -> {
                bakeryDao.insertUser(new UserEntity("uid_" + email.hashCode(), nickname, email, ""));
            });
            result.postValue(true);
        } else {
            result.postValue(false);
        }
        return result;
    }

    @Override
    public void logout() {
        sharedPreferences.edit().clear().apply();
        clearCart();
    }

    @Override
    public boolean isAuthorized() {
        String id = sharedPreferences.getString("client_id", "");
        return !id.isEmpty() && !id.startsWith("guest_");
    }

    @Override
    public LiveData<User> getClientInfo() {
        MutableLiveData<User> result = new MutableLiveData<>();
        String id = sharedPreferences.getString("client_id", "uid_guest");
        String email = sharedPreferences.getString("client_email", "guest@bakery.com");
        String nickname = sharedPreferences.getString("client_nickname", "Гость");
        int orderCount = sharedPreferences.getInt("client_order_count", 3);
        result.setValue(new User(id, nickname, email, "", orderCount));
        return result;
    }

    @Override
    public void saveClientInfo(User user) {
        sharedPreferences.edit()
                .putString("client_id", user.getId())
                .putString("client_email", user.getEmail())
                .putString("client_nickname", user.getNickname())
                .putInt("client_order_count", user.getOrderCount())
                .apply();
    }

    @Override
    public LiveData<List<Product>> getProducts() {
        MediatorLiveData<List<Product>> mediator = new MediatorLiveData<>();
        LiveData<List<ProductEntity>> dbSource = bakeryDao.getAllProducts();
        LiveData<List<ProductEntity>> netSource = networkApi.getMockProducts();

        mediator.addSource(netSource, networkProducts -> {
            if (networkProducts != null && !networkProducts.isEmpty()) {
                executor.execute(() -> bakeryDao.insertProducts(networkProducts));
            }
        });

        mediator.addSource(dbSource, dbProducts -> {
            if (dbProducts != null) {
                List<Product> products = new ArrayList<>();
                for (ProductEntity e : dbProducts) {
                    products.add(new Product(e.id, e.name, e.categoryId, e.weightOrVolume, e.unit, e.description, e.price, e.imageUrl));
                }
                mediator.setValue(products);
            }
        });

        return mediator;
    }

    @Override
    public LiveData<List<Category>> getCategories() {
        MediatorLiveData<List<Category>> mediator = new MediatorLiveData<>();
        LiveData<List<CategoryEntity>> dbSource = bakeryDao.getAllCategories();
        LiveData<List<CategoryEntity>> netSource = networkApi.getMockCategories();

        mediator.addSource(netSource, networkCategories -> {
            if (networkCategories != null && !networkCategories.isEmpty()) {
                executor.execute(() -> bakeryDao.insertCategories(networkCategories));
            }
        });

        mediator.addSource(dbSource, dbCategories -> {
            if (dbCategories != null) {
                List<Category> categories = new ArrayList<>();
                for (CategoryEntity e : dbCategories) {
                    categories.add(new Category(e.id, e.name));
                }
                mediator.setValue(categories);
            }
        });

        return mediator;
    }

    @Override
    public LiveData<List<Comment>> getComments(int productId) {
        MediatorLiveData<List<Comment>> mediator = new MediatorLiveData<>();
        mediator.addSource(bakeryDao.getCommentsForProduct(productId), entities -> {
            if (entities != null) {
                List<Comment> list = new ArrayList<>();
                for (CommentEntity e : entities) {
                    list.add(new Comment(e.id, e.text, e.userId, e.productId));
                }
                mediator.setValue(list);
            }
        });
        return mediator;
    }

    @Override
    public LiveData<List<Order>> getOrders() {
        String userId = sharedPreferences.getString("client_id", "uid_guest");
        MediatorLiveData<List<Order>> mediator = new MediatorLiveData<>();
        mediator.addSource(bakeryDao.getOrdersForUser(userId), entities -> {
            if (entities != null) {
                List<Order> list = new ArrayList<>();
                for (OrderEntity e : entities) {
                    list.add(new Order(e.id, e.userId, e.cost, e.itemCount));
                }
                mediator.setValue(list);
            }
        });
        return mediator;
    }

    @Override
    public void addToCart(Product product) {
        Product existingKey = null;
        for (Product p : cartMap.keySet()) {
            if (p.getId() == product.getId()) {
                existingKey = p;
                break;
            }
        }
        if (existingKey != null) {
            cartMap.put(existingKey, cartMap.get(existingKey) + 1);
        } else {
            cartMap.put(product, 1);
        }
        cartItemsLiveData.setValue(new HashMap<>(cartMap));
    }

    @Override
    public void removeFromCart(Product product) {
        Product existingKey = null;
        for (Product p : cartMap.keySet()) {
            if (p.getId() == product.getId()) {
                existingKey = p;
                break;
            }
        }
        if (existingKey != null) {
            int count = cartMap.get(existingKey);
            if (count > 1) {
                cartMap.put(existingKey, count - 1);
            } else {
                cartMap.remove(existingKey);
            }
        }
        cartItemsLiveData.setValue(new HashMap<>(cartMap));
    }

    @Override
    public void clearCart() {
        cartMap.clear();
        cartItemsLiveData.setValue(new HashMap<>(cartMap));
    }

    @Override
    public LiveData<Map<Product, Integer>> getCartItems() {
        return cartItemsLiveData;
    }

    @Override
    public void createOrder(double cost, int itemCount) {
        String userId = sharedPreferences.getString("client_id", "uid_guest");
        int currentOrders = sharedPreferences.getInt("client_order_count", 0);
        sharedPreferences.edit().putInt("client_order_count", currentOrders + 1).apply();

        executor.execute(() -> {
            int orderId = (int) (System.currentTimeMillis() % 100000);
            bakeryDao.insertOrder(new OrderEntity(orderId, userId, cost, itemCount));
        });
        clearCart();
    }
}
