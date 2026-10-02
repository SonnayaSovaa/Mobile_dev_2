package ru.mirea.nagishevakv.backeryproject.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class OrderEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String userId;
    public double cost;
    public int itemCount;
    public String itemsDescription;
    public String date;
    public String city;
    public String status;

    public OrderEntity(String userId, double cost, int itemCount, String itemsDescription, String date, String city, String status) {
        this.userId = userId;
        this.cost = cost;
        this.itemCount = itemCount;
        this.itemsDescription = itemsDescription;
        this.date = date;
        this.city = city;
        this.status = status;
    }
}