package ru.mirea.nagishevakv.backeryproject.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class OrderEntity {
    @PrimaryKey
    public int id;
    public String userId;
    public double cost;
    public int itemCount;

    public OrderEntity() {}

    public OrderEntity(int id, String userId, double cost, int itemCount) {
        this.id = id;
        this.userId = userId;
        this.cost = cost;
        this.itemCount = itemCount;
    }
}