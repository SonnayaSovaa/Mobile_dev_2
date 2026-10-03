package ru.mirea.nagishevakv.backeryproject.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "comments")
public class CommentEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String text;
    public String userId;
    public int productId;
    public String userName;
    public String userPhotoUrl;
    public String date;

    public CommentEntity(String text, String userId, int productId, String userName, String userPhotoUrl, String date) {
        this.text = text;
        this.userId = userId;
        this.productId = productId;
        this.userName = userName;
        this.userPhotoUrl = userPhotoUrl;
        this.date = date;
    }
}