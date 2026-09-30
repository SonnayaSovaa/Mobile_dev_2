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
        
        // Category 1: Выпечка (Круассаны)
        products.add(new Product(1, "Классический круассан", 1, 80, "г", "Нежный хрустящий круассан на сливочном масле", 150.0, "croissant.jpg"));
        products.add(new Product(4, "Миндальный круассан", 1, 95, "г", "Круассан с миндальным кремом и лепестками", 180.0, "almond_croissant.jpg"));
        
        // Category 2: Напитки
        products.add(new Product(2, "Капучино", 2, 300, "мл", "Ароматный кофе с густой пеной", 150.0, "cappuccino.jpg"));
        products.add(new Product(3, "Айс Капучино", 2, 350, "мл", "Освежающий кофе со льдом и нежной пенкой", 220.0, "ice_cappuccino.jpg"));
        products.add(new Product(6, "Лимонад Цитрус", 2, 400, "мл", "Натуральный лимонад с лимоном и апельсином", 200.0, "lemonade.jpg"));
        
        // Category 3: Пончики
        products.add(new Product(5, "Шоколадный пончик", 3, 90, "г", "Пончик с шоколадной глазурью и начинкой", 120.0, "donut_choco.jpg"));
        products.add(new Product(7, "Клубничный донат", 3, 85, "г", "Яркий пончик с клубничным вкусом", 130.0, "donut_strawberry.jpg"));
        products.add(new Product(8, "Карамельный пончик", 3, 90, "г", "Пончик с мягкой карамелью внутри", 140.0, "donut_caramel.jpg"));

        // Category 4: Хлеб
        products.add(new Product(9, "Багет французский", 4, 250, "г", "Традиционный пшеничный багет с хрустящей корочкой", 95.0, "baguette.jpg"));
        products.add(new Product(10, "Бородинский хлеб", 4, 400, "г", "Ржаной хлеб с кориандром", 80.0, "borodinsky.jpg"));

        data.setValue(products);
        return data;
    }

    public LiveData<List<Category>> getCategories() {
        MutableLiveData<List<Category>> data = new MutableLiveData<>();
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "Выпечка"));
        categories.add(new Category(2, "Напитки"));
        categories.add(new Category(3, "Пончики"));
        categories.add(new Category(4, "Хлеб"));
        data.setValue(categories);
        return data;
    }
}