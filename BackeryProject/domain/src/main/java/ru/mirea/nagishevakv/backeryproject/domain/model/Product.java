package ru.mirea.nagishevakv.backeryproject.domain.model;

public class Product {
    private final int id;
    private final String name;
    private final int categoryId;
    private final double weightOrVolume;
    private final String unit;
    private final String description;
    private final int price;
    private final String imageUrl;

    public Product(int id, String name, int categoryId, double weightOrVolume, String unit, String description, int price, String imageUrl) {
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
    public int getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
}