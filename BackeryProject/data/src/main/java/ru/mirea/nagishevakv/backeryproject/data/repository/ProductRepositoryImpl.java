package ru.mirea.nagishevakv.backeryproject.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import ru.mirea.nagishevakv.backeryproject.data.local.dao.BakeryDao;
import ru.mirea.nagishevakv.backeryproject.data.local.db.BakeryDatabase;
import ru.mirea.nagishevakv.backeryproject.data.local.entity.ProductEntity;
import ru.mirea.nagishevakv.backeryproject.data.network.NetworkApi;
import ru.mirea.nagishevakv.backeryproject.domain.model.Product;
import ru.mirea.nagishevakv.backeryproject.domain.repository.ProductRepository;

public class ProductRepositoryImpl implements ProductRepository {
    private static ProductRepositoryImpl INSTANCE;
    private final BakeryDao bakeryDao;
    private final NetworkApi networkApi;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final MutableLiveData<Map<Product, Integer>> cartItems = new MutableLiveData<>(new HashMap<>());

    private ProductRepositoryImpl(Context context) {
        this.bakeryDao = BakeryDatabase.getDatabase(context).bakeryDao();
        this.networkApi = new NetworkApi();
    }

    public static synchronized ProductRepositoryImpl getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ProductRepositoryImpl(context.getApplicationContext());
        }
        return INSTANCE;
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
}
