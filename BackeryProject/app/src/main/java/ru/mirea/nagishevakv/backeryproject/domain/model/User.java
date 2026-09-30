package ru.mirea.nagishevakv.backeryproject.domain.model;

public class User {
    private String id;
    private String nickname;
    private String email;
    private String photoUrl;
    private int orderCount;

    public User(String id, String nickname, String email, String photoUrl, int orderCount) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.photoUrl = photoUrl;
        this.orderCount = orderCount;
    }

    public String getId() { return id; }
    public String getNickname() { return nickname; }
    public String getEmail() { return email; }
    public String getPhotoUrl() { return photoUrl; }
    public int getOrderCount() { return orderCount; }
}