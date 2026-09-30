package ru.mirea.nagishevakv.backeryproject.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class ProductEntity {
    @PrimaryKey
    public int id;
    public String name;
    public int categoryId;
    public double weightOrVolume;
    public String unit;
    public String description;
    public double price;
    public String imageUrl;

    public ProductEntity() {}

    public ProductEntity(int id, String name, int categoryId, double weightOrVolume, String unit, String description, double price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.weightOrVolume = weightOrVolume;
        this.unit = unit;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}