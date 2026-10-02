package ru.mirea.nagishevakv.backeryproject.domain.repository;

import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.Map;
import ru.mirea.nagishevakv.backeryproject.domain.model.Category;
import ru.mirea.nagishevakv.backeryproject.domain.model.Product;
import ru.mirea.nagishevakv.backeryproject.domain.model.User;
import ru.mirea.nagishevakv.backeryproject.domain.model.Order;
import ru.mirea.nagishevakv.backeryproject.domain.model.Comment;

public interface BakeryRepository {
    // Auth logic
    LiveData<Boolean> login(String email, String password);
    LiveData<Boolean> register(String email, String password, String nickname);
    void logout();
    boolean isAuthorized();
    
    // SharedPreferences & User Profile
    LiveData<User> getClientInfo();
    void saveClientInfo(User user);
    
    // Data from Room / Network
    LiveData<List<Product>> getProducts();
    LiveData<List<Category>> getCategories();
    LiveData<List<Comment>> getComments(int productId);
    LiveData<List<Order>> getOrders();
    
    // Cart
    void addToCart(Product product);
    void removeFromCart(Product product);
    void clearCart();
    LiveData<Map<Product, Integer>> getCartItems();
    
    // Order
    void createOrder(double cost, int itemCount, String itemsDescription, String city);
}