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
    public String photo;

    public UserEntity() {
        this.id = "";
    }

    public UserEntity(@NonNull String id, String nickname, String email, String photo) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.photo = photo;
    }
}