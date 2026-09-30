package ru.mirea.nagishevakv.backeryproject.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String nickname;
    public String email;
    public String photoUrl;
    public int orderCount;

    public UserEntity(@NonNull String id, String nickname, String email, String photoUrl, int orderCount) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.photoUrl = photoUrl;
        this.orderCount = orderCount;
    }
}