package ru.mirea.nagishevakv.backeryproject.domain.model;

public class Product {
    private int id;
    private String name;
    private int categoryId;
    private double weightOrVolume;
    private String unit; // "г" or "мл"
    private String description;
    private double price;
    private String imageUrl;

    public Product(int id, String name, int categoryId, double weightOrVolume, String unit, String description, double price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.weightOrVolume = weightOrVolume;
        this.unit = unit;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getCategoryId() { return categoryId; }
    public double getWeightOrVolume() { return weightOrVolume; }
    public String getUnit() { return unit; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
}