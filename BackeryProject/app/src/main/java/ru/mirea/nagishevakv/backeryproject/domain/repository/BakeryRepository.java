package ru.mirea.nagishevakv.backeryproject.domain.repository;

import androidx.lifecycle.LiveData;
import java.util.List;
import ru.mirea.nagishevakv.backeryproject.domain.model.Category;
import ru.mirea.nagishevakv.backeryproject.domain.model.Product;
import ru.mirea.nagishevakv.backeryproject.domain.model.User;
import ru.mirea.nagishevakv.backeryproject.domain.model.Order;
import ru.mirea.nagishevakv.backeryproject.domain.model.Comment;

public interface BakeryRepository {
    // Auth logic distributed/handled via Use Cases & Repositories
    LiveData<Boolean> loginWithEmailAndPassword(String email, String password);
    LiveData<Boolean> registerWithEmailAndPassword(String email, String password, String nickname);
    
    // SharedPreferences & User Profile Client Info
    LiveData<User> getClientInfo();
    void saveClientInfo(User user);
    
    // Catalog & Room / Network Api data via MediatorLiveData
    LiveData<List<Product>> getProducts();
    LiveData<List<Category>> getCategories();
    LiveData<List<Comment>> getCommentsForProduct(int productId);
    LiveData<List<Order>> getUserOrders(String userId);
    
    // Cart operations
    void addToCart(Product product);
    void removeFromCart(Product product);
    void clearCart();
    LiveData<java.util.Map<Product, Integer>> getCartItems();
    
    // Order placements
    void placeOrder(double cost, int itemCount);
}