package ru.mirea.nagishevakv.backeryproject.presentation.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Map;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mirea.nagishevakv.backeryproject.data.network.weather.WeatherApi;
import ru.mirea.nagishevakv.backeryproject.data.network.weather.WeatherResponse;
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
    private final MutableLiveData<String> weatherData = new MutableLiveData<>();

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

    public LiveData<String> getWeatherData() { return weatherData; }

    public void fetchWeather(String city) {
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
                    String result = String.format(Locale.getDefault(), 
                        "Город: %s\nТемпература: %.1f°C\nОщущается как: %.1f°C\nВлажность: %d%%\nОписание: %s",
                        w.name, w.main.temp, w.main.feels_like, w.main.humidity, w.weather[0].description);
                    weatherData.postValue(result);
                } else {
                    weatherData.postValue("Ошибка получения данных: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                weatherData.postValue("Ошибка сети: " + t.getMessage());
            }
        });
    }
}
