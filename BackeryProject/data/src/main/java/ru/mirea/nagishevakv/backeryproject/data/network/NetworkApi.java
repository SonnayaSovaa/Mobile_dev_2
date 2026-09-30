package ru.mirea.nagishevakv.backeryproject.data.network;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.nagishevakv.backeryproject.domain.model.Category;
import ru.mirea.nagishevakv.backeryproject.domain.model.Product;

public class NetworkApi {
    public LiveData<List<Product>> getProducts() {
        MutableLiveData<List<Product>> data = new MutableLiveData<>();
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Круассан с шоколадом", 1, 80, "г", "Классический круассан", 120, "croissant.jpg"));
        products.add(new Product(2, "Капучино", 2, 300, "мл", "Ароматный кофе", 150, "cappuccino.jpg"));
        products.add(new Product(3, "Пончик ванильный", 3, 60, "г", "Сладкий пончик", 90, "donut.jpg"));
        data.setValue(products);
        return data;
    }

    public LiveData<List<Category>> getCategories() {
        MutableLiveData<List<Category>> data = new MutableLiveData<>();
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "Выпечка"));
        categories.add(new Category(2, "Горячие напитки"));
        categories.add(new Category(3, "Пончики"));
        data.setValue(categories);
        return data;
    }
}