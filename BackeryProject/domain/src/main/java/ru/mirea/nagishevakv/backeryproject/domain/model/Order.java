package ru.mirea.nagishevakv.backeryproject.domain.model;

public class Order {
    private final int id;
    private final String userId;
    private final double cost;
    private final int itemCount;
    private final String itemsDescription;
    private final String date;
    private final String city;
    private final String status;

    public Order(int id, String userId, double cost, int itemCount, String itemsDescription, String date, String city, String status) {
        this.id = id;
        this.userId = userId;
        this.cost = cost;
        this.itemCount = itemCount;
        this.itemsDescription = itemsDescription;
        this.date = date;
        this.city = city;
        this.status = status;
    }

    public int getId() { return id; }
    public String getUserId() { return userId; }
    public double getCost() { return cost; }
    public int getItemCount() { return itemCount; }
    public String getItemsDescription() { return itemsDescription; }
    public String getDate() { return date; }
    public String getCity() { return city; }
    public String getStatus() { return status; }
}