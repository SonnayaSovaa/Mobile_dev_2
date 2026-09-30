package ru.mirea.nagishevakv.backeryproject.presentation.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Map;

import ru.mirea.nagishevakv.backeryproject.data.repository.BakeryRepositoryImpl;
import ru.mirea.nagishevakv.backeryproject.domain.model.Category;
import ru.mirea.nagishevakv.backeryproject.domain.model.Product;
import ru.mirea.nagishevakv.backeryproject.domain.model.User;
import ru.mirea.nagishevakv.backeryproject.domain.repository.BakeryRepository;
import ru.mirea.nagishevakv.backeryproject.domain.usecase.GetCartUseCase;
import ru.mirea.nagishevakv.backeryproject.domain.usecase.GetProductsUseCase;
import ru.mirea.nagishevakv.backeryproject.domain.usecase.LoginUseCase;
import ru.mirea.nagishevakv.backeryproject.domain.usecase.ManageCartUseCase;
import ru.mirea.nagishevakv.backeryproject.domain.usecase.RegisterUseCase;

public class BakeryViewModel extends AndroidViewModel {
    private final BakeryRepository repository;
    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;
    private final GetProductsUseCase getProductsUseCase;
    private final GetCartUseCase getCartUseCase;
    private final ManageCartUseCase manageCartUseCase;

    private final MutableLiveData<String> currentScreen = new MutableLiveData<>("AUTH");
    private final MutableLiveData<Product> selectedProduct = new MutableLiveData<>();

    public BakeryViewModel(@NonNull Application application) {
        super(application);
        this.repository = BakeryRepositoryImpl.getInstance(application);
        this.loginUseCase = new LoginUseCase(repository);
        this.registerUseCase = new RegisterUseCase(repository);
        this.getProductsUseCase = new GetProductsUseCase(repository);
        this.getCartUseCase = new GetCartUseCase(repository);
        this.manageCartUseCase = new ManageCartUseCase(repository);
    }

    public LiveData<String> getCurrentScreen() { return currentScreen; }
    public void navigateTo(String screen) { currentScreen.setValue(screen); }

    public LiveData<Product> getSelectedProduct() { return selectedProduct; }
    public void selectProduct(Product product) { selectedProduct.setValue(product); navigateTo("DETAIL"); }

    public LiveData<Boolean> login(String email, String password) {
        return loginUseCase.execute(email, password);
    }

    public void loginAsGuest() {
        User guest = new User("guest_" + System.currentTimeMillis(), "Гость", "", "", 0);
        repository.saveClientInfo(guest);
    }

    public boolean isAuthorized() {
        return repository.isAuthorized();
    }

    public void logout() {
        repository.logout();
        navigateTo("AUTH");
    }

    public LiveData<Boolean> register(String email, String password, String nickname) {
        return registerUseCase.execute(email, password, nickname);
    }

    public LiveData<List<Product>> getProducts() { return getProductsUseCase.execute(); }
    public LiveData<List<Category>> getCategories() { return repository.getCategories(); }
    public LiveData<User> getClientInfo() { return repository.getClientInfo(); }
    public LiveData<Map<Product, Integer>> getCartItems() { return getCartUseCase.execute(); }

    public void addToCart(Product product) { manageCartUseCase.add(product); }
    public void removeFromCart(Product product) { manageCartUseCase.remove(product); }
    public void clearCart() { manageCartUseCase.clear(); }
    public void checkout(double cost, int itemCount) { repository.createOrder(cost, itemCount); }
}
