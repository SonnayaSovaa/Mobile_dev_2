package ru.mirea.nagishevakv.backeryproject.domain.model;

public class User {
    private final String id;
    private final String nickname;
    private final String email;
    private final String photoUrl;
    private final int orderCount;
    private final double rating;

    public User(String id, String nickname, String email, String photoUrl, int orderCount, double rating) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.photoUrl = photoUrl;
        this.orderCount = orderCount;
        this.rating = rating;
    }

    public String getId() { return id; }
    public String getNickname() { return nickname; }
    public String getEmail() { return email; }
    public String getPhotoUrl() { return photoUrl; }
    public int getOrderCount() { return orderCount; }
    public double getRating() { return rating; }
}