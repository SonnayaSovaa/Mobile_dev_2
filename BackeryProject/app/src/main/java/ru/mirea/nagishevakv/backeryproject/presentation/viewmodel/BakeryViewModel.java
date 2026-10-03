package ru.mirea.nagishevakv.backeryproject.presentation.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mirea.nagishevakv.backeryproject.data.network.weather.WeatherApi;
import ru.mirea.nagishevakv.backeryproject.data.network.weather.WeatherResponse;
import ru.mirea.nagishevakv.backeryproject.data.repository.BakeryRepositoryImpl;
import ru.mirea.nagishevakv.backeryproject.domain.model.Category;
import ru.mirea.nagishevakv.backeryproject.domain.model.Comment;
import ru.mirea.nagishevakv.backeryproject.domain.model.Order;
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
    
    // Weather & City sync
    private final MutableLiveData<String> selectedCity = new MutableLiveData<>("Выберите город");
    private final MutableLiveData<String> temperature = new MutableLiveData<>("");
    private final MutableLiveData<String> weatherDescription = new MutableLiveData<>("");
    
    // Filters
    private final MutableLiveData<String> nameFilter = new MutableLiveData<>("");
    private final MutableLiveData<Double> maxPriceFilter = new MutableLiveData<>(Double.MAX_VALUE);
    private final MutableLiveData<Integer> categoryFilter = new MutableLiveData<>(-1); // -1 for all
    
    // Discount state
    private final MutableLiveData<Integer> discountCategoryId = new MutableLiveData<>(-1);
    private final MutableLiveData<String> discountKeyword = new MutableLiveData<>("");

    private final MediatorLiveData<List<Product>> filteredProducts = new MediatorLiveData<>();
    private final LiveData<List<Product>> productsSource;

    public BakeryViewModel(@NonNull Application application) {
        super(application);
        this.repository = BakeryRepositoryImpl.getInstance(application);
        this.loginUseCase = new LoginUseCase(repository);
        this.registerUseCase = new RegisterUseCase(repository);
        this.getProductsUseCase = new GetProductsUseCase(repository);
        this.getCartUseCase = new GetCartUseCase(repository);
        this.manageCartUseCase = new ManageCartUseCase(repository);

        this.productsSource = getProductsUseCase.execute();
        filteredProducts.addSource(productsSource, products -> applyFilters());
        filteredProducts.addSource(nameFilter, filter -> applyFilters());
        filteredProducts.addSource(maxPriceFilter, filter -> applyFilters());
        filteredProducts.addSource(categoryFilter, filter -> applyFilters());
    }

    private void applyFilters() {
        List<Product> products = productsSource.getValue();
        if (products == null) return;

        String name = nameFilter.getValue().toLowerCase();
        double price = maxPriceFilter.getValue();
        int catId = categoryFilter.getValue();

        List<Product> result = products.stream()
                .filter(p -> p.getName().toLowerCase().contains(name))
                .filter(p -> p.getPrice() <= price)
                .filter(p -> catId == -1 || p.getCategoryId() == catId)
                .collect(Collectors.toList());
        
        filteredProducts.setValue(result);
    }

    public LiveData<String> getCurrentScreen() { return currentScreen; }
    public void navigateTo(String screen) { currentScreen.setValue(screen); }
    public LiveData<Product> getSelectedProduct() { return selectedProduct; }
    public void selectProduct(Product product) { selectedProduct.setValue(product); navigateTo("DETAIL"); }
    public LiveData<Boolean> login(String email, String password) { return loginUseCase.execute(email, password); }
    public void loginAsGuest() {
        User guest = new User("guest_" + System.currentTimeMillis(), "Гость", "", "", 0);
        repository.saveClientInfo(guest);
    }
    public boolean isAuthorized() { return repository.isAuthorized(); }
    public void logout() { repository.logout(); navigateTo("AUTH"); }
    public LiveData<Boolean> register(String email, String password, String nickname) { return registerUseCase.execute(email, password, nickname); }
    public LiveData<List<Product>> getProducts() { return filteredProducts; }
    public LiveData<List<Category>> getCategories() { return repository.getCategories(); }
    public LiveData<User> getClientInfo() { return repository.getClientInfo(); }
    public void updateAvatar(String photoUrl) { repository.updateUserPhoto(photoUrl); }
    public LiveData<List<Order>> getOrders() { return repository.getOrders(); }

    public LiveData<List<Comment>> getComments(int productId) { return repository.getComments(productId); }
    public void postComment(int productId, String text) {
        User user = repository.getClientInfo().getValue();
        if (user == null || text.trim().isEmpty()) return;
        
        String date = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(new Date());
        Comment comment = new Comment(0, text, user.getId(), productId, user.getNickname(), user.getPhotoUrl(), date);
        repository.addComment(comment);
    }
    
    public void deleteComment(int commentId) {
        repository.deleteComment(commentId);
    }

    public void setNameFilter(String name) { nameFilter.setValue(name); }
    public void setMaxPriceFilter(Double price) { maxPriceFilter.setValue(price == null ? Double.MAX_VALUE : price); }
    public void setCategoryFilter(int categoryId) { categoryFilter.setValue(categoryId); }
    public LiveData<Integer> getCategoryFilter() { return categoryFilter; }

    public LiveData<Integer> getDiscountCategoryId() { return discountCategoryId; }
    public LiveData<String> getDiscountKeyword() { return discountKeyword; }

    public void addToCart(Product product) { manageCartUseCase.add(product); }
    public void removeFromCart(Product product) { manageCartUseCase.remove(product); }
    public void clearCart() { manageCartUseCase.clear(); }
    public LiveData<Map<Product, Integer>> getCartItems() { return getCartUseCase.execute(); }
    public void checkout(double cost, int itemCount, String desc, String city) { repository.createOrder(cost, itemCount, desc, city); }

    public LiveData<String> getTemperature() { return temperature; }
    public LiveData<String> getWeatherDescription() { return weatherDescription; }
    public LiveData<String> getSelectedCity() { return selectedCity; }

    public void fetchWeather(String city) {
        selectedCity.setValue(city);
        if (city == null || city.isEmpty() || city.equals("Выберите город")) {
            temperature.setValue("");
            weatherDescription.setValue("");
            discountCategoryId.setValue(-1);
            discountKeyword.setValue("");
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi api = retrofit.create(WeatherApi.class);
        api.getWeather(city, "987f8a3ed5f7f76fb867faa32c144231", "metric", "ru").enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse w = response.body();
                    temperature.postValue(String.format(Locale.getDefault(), "%.1f°C", w.main.temp));
                    weatherDescription.postValue(w.weather[0].description);
                    calculateDiscounts(w);
                } else {
                    temperature.postValue("Ошибка");
                    weatherDescription.postValue(String.valueOf(response.code()));
                    discountCategoryId.postValue(-1);
                    discountKeyword.postValue("");
                }
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                temperature.postValue("Ошибка");
                weatherDescription.postValue("Нет сети");
            }
        });
    }

    private void calculateDiscounts(WeatherResponse w) {
        float temp = w.main.temp;
        String desc = w.weather[0].description.toLowerCase();
        
        int catId = -1;
        String keyword = "";

        if (desc.contains("дожд") || desc.contains("rain")) {
            catId = 1; // Выпечка
        } else if (temp < 10) {
            catId = 2; // Горячие напитки
            keyword = "HOT"; 
        } else if (temp > 25) {
            catId = 6; // Холодные напитки
            keyword = "COLD";
        } else if (desc.contains("солн") || desc.contains("clear") || desc.contains("ясно")) {
            catId = 5; // Десерты
            keyword = "мороженое";
        }
        
        discountCategoryId.postValue(catId);
        discountKeyword.postValue(keyword);
    }
}