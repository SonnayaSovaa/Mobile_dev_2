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

    public OrderEntity(String userId, double cost, int itemCount) {
        this.userId = userId;
        this.cost = cost;
        this.itemCount = itemCount;
    }
}