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
    LiveData<Boolean> login(String email, String password);
    LiveData<Boolean> register(String email, String password, String nickname);
    void logout();
    boolean isAuthorized();
    
    LiveData<User> getClientInfo();
    void saveClientInfo(User user);
    void updateUserPhoto(String photoUrl);
    void updateUserRating(double delta);
    
    LiveData<List<Product>> getProducts();
    LiveData<List<Category>> getCategories();
    LiveData<List<Comment>> getComments(int productId);
    void addComment(Comment comment);
    void deleteComment(int commentId);
    LiveData<List<Order>> getOrders();
    
    void addToCart(Product product);
    void removeFromCart(Product product);
    void clearCart();
    LiveData<Map<Product, Integer>> getCartItems();
    
    void createOrder(double cost, int itemCount, String itemsDescription, String city);
}
