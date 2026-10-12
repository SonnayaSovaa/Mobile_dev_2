package ru.mirea.nagishevakv.backeryproject.domain.repository;

import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.Map;
import ru.mirea.nagishevakv.backeryproject.domain.model.Product;

public interface ProductRepository {
    LiveData<List<Product>> getProducts();
    void addToCart(Product product);
    void removeFromCart(Product product);
    void clearCart();
    LiveData<Map<Product, Integer>> getCartItems();
}
