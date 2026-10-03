package ru.mirea.nagishevakv.backeryproject.domain.model;

public class Comment {
    private final int id;
    private final String text;
    private final String userId;
    private final int productId;
    private final String userName;
    private final String userPhotoUrl;
    private final String date;

    public Comment(int id, String text, String userId, int productId, String userName, String userPhotoUrl, String date) {
        this.id = id;
        this.text = text;
        this.userId = userId;
        this.productId = productId;
        this.userName = userName;
        this.userPhotoUrl = userPhotoUrl;
        this.date = date;
    }

    public int getId() { return id; }
    public String getText() { return text; }
    public String getUserId() { return userId; }
    public int getProductId() { return productId; }
    public String getUserName() { return userName; }
    public String getUserPhotoUrl() { return userPhotoUrl; }
    public String getDate() { return date; }
}