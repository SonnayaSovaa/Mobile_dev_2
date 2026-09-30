package ru.mirea.nagishevakv.backeryproject.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "comments")
public class CommentEntity {
    @PrimaryKey
    public int id;
    public String text;
    public String userId;
    public int productId;

    public CommentEntity() {}

    public CommentEntity(int id, String text, String userId, int productId) {
        this.id = id;
        this.text = text;
        this.userId = userId;
        this.productId = productId;
    }
}