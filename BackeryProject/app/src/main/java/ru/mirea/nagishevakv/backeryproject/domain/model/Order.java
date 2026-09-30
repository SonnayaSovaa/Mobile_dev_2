package ru.mirea.nagishevakv.backeryproject.domain.model;

public class Order {
    private int id;
    private String userId;
    private double cost;
    private int itemCount;

    public Order(int id, String userId, double cost, int itemCount) {
        this.id = id;
        this.userId = userId;
        this.cost = cost;
        this.itemCount = itemCount;
    }

    public int getId() { return id; }
    public String getUserId() { return userId; }
    public double getCost() { return cost; }
    public int getItemCount() { return itemCount; }
}