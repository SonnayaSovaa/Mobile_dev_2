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
        
        // Category 1: Выпечка
        products.add(new Product(1, "Классический круассан", 1, 80, "г", "Нежный хрустящий круассан на сливочном масле", 150, "Classic_croissant.jpg"));
        products.add(new Product(4, "Миндальный круассан", 1, 95, "г", "Круассан с миндальным кремом и лепестками", 180, "Almond_croissant.jpg"));
        
        // Category 2: Горячие напитки
        products.add(new Product(2, "Капучино", 2, 300, "мл", "Ароматный кофе с густой пеной", 150, "Capuccino.jpg"));
        products.add(new Product(13, "Латте", 2, 350, "мл", "Нежный кофейный напиток с большим количеством молока", 170, "Latte.jpg"));
        
        // Category 6: Холодные напитки
        products.add(new Product(3, "Айс Капучино", 6, 350, "мл", "Освежающий кофе со льдом и нежной пенкой", 220, "Ice_cappucino.png"));
        products.add(new Product(14, "Айс Латте", 6, 400, "мл", "Холодный латте со льдом", 240, "Ice_latte.jpg"));
        products.add(new Product(6, "Лимонад", 6, 400, "мл", "Натуральный лимонад с лимоном и апельсином", 200, "Lemonade.jpg"));
        
        // Category 3: Пончики
        products.add(new Product(5, "Шоколадный пончик", 3, 90, "г", "Пончик с шоколадной глазурью и начинкой", 120, "Chocolate_donut.png"));
        products.add(new Product(7, "Клубничный донат", 3, 85, "г", "Яркий пончик с клубничным вкусом", 130, "Strawberry_donut.jpg"));
        products.add(new Product(8, "Карамельный пончик", 3, 90, "г", "Пончик с мягкой карамелью внутри", 140, "Caramel_donut.jpg"));

        // Category 4: Хлеб
        products.add(new Product(9, "Багет французский", 4, 250, "г", "Традиционный пшеничный багет с хрустящей корочкой", 95, "Baguette.jpg"));
        products.add(new Product(10, "Бородинский хлеб", 4, 400, "г", "Ржаной хлеб с кориандром", 80, "Borodinskiy_hleb.jpg"));

        // Category 5: Десерты
        products.add(new Product(11, "Ванильное мороженое", 5, 100, "г", "Классический пломбир", 100, "Vanilla_icecream.jpg"));
        products.add(new Product(12, "Шоколадное мороженое", 5, 120, "г", "Сливочное шоколадное мороженое", 120, "Chocolate_icecream.jpg"));

        data.setValue(products);
        return data;
    }

    public LiveData<List<Category>> getCategories() {
        MutableLiveData<List<Category>> data = new MutableLiveData<>();
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "Выпечка"));
        categories.add(new Category(2, "Горячие напитки"));
        categories.add(new Category(6, "Холодные напитки"));
        categories.add(new Category(3, "Пончики"));
        categories.add(new Category(4, "Хлеб"));
        categories.add(new Category(5, "Десерты"));
        data.setValue(categories);
        return data;
    }
}