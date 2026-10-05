package ru.mirea.nagishevakv.backeryproject.domain.model;

public class Comment {
    private final int id;
    private final String text;
    private final String userId;
    private final int productId;
    private final String userName;
    private final String userPhotoUrl;
    private final String date;
    private final double userRating;

    public Comment(int id, String text, String userId, int productId, String userName, String userPhotoUrl, String date, double userRating) {
        this.id = id;
        this.text = text;
        this.userId = userId;
        this.productId = productId;
        this.userName = userName;
        this.userPhotoUrl = userPhotoUrl;
        this.date = date;
        this.userRating = userRating;
    }

    public int getId() { return id; }
    public String getText() { return text; }
    public String getUserId() { return userId; }
    public int getProductId() { return productId; }
    public String getUserName() { return userName; }
    public String getUserPhotoUrl() { return userPhotoUrl; }
    public String getDate() { return date; }
    public double getUserRating() { return userRating; }
}