package ru.mirea.nagishevakv.backeryproject.data.network;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.nagishevakv.backeryproject.data.local.entity.CategoryEntity;
import ru.mirea.nagishevakv.backeryproject.data.local.entity.ProductEntity;

public class NetworkApi {
    private static NetworkApi instance;

    private NetworkApi() {}

    public static synchronized NetworkApi getInstance() {
        if (instance == null) {
            instance = new NetworkApi();
        }
        return instance;
    }

    public LiveData<List<CategoryEntity>> getMockCategories() {
        MutableLiveData<List<CategoryEntity>> data = new MutableLiveData<>();
        List<CategoryEntity> list = new ArrayList<>();
        list.add(new CategoryEntity(1, "Круассаны"));
        list.add(new CategoryEntity(2, "Холодные напитки"));
        list.add(new CategoryEntity(3, "Пончики"));
        data.postValue(list);
        return data;
    }

    public LiveData<List<ProductEntity>> getMockProducts() {
        MutableLiveData<List<ProductEntity>> data = new MutableLiveData<>();
        List<ProductEntity> list = new ArrayList<>();
        list.add(new ProductEntity(1, "Классический круассан", 1, 80, "граммы", "Нежный хрустящий круассан на сливочном масле", 150.0, ""));
        list.add(new ProductEntity(2, "Шоколадный пончик", 3, 90, "граммы", "Пончик с шоколадной глазурью и начинкой", 120.0, ""));
        list.add(new ProductEntity(3, "Айс Капучино", 2, 350, "миллилитры", "Освежающий кофе со льдом и нежной пенкой", 220.0, ""));
        list.add(new ProductEntity(4, "Миндальный круассан", 1, 95, "граммы", "Круассан с миндальным кремом и лепестками", 180.0, ""));
        list.add(new ProductEntity(5, "Клубничный донат", 3, 85, "граммы", "Яркий пончик с клубничным вкусом", 130.0, ""));
        list.add(new ProductEntity(6, "Лимонад Цитрус", 2, 400, "миллилитры", "Натуральный лимонад с лимоном и апельсином", 200.0, ""));
        list.add(new ProductEntity(7, "Карамельный пончик", 3, 90, "граммы", "Пончик с мягкой карамелью внутри", 140.0, ""));
        data.postValue(list);
        return data;
    }
}