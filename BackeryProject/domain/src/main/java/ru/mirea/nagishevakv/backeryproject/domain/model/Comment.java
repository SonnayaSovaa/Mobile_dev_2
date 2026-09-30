package ru.mirea.nagishevakv.backeryproject.domain.model;

public class Comment {
    private final int id;
    private final String text;
    private final String userId;
    private final int productId;

    public Comment(int id, String text, String userId, int productId) {
        this.id = id;
        this.text = text;
        this.userId = userId;
        this.productId = productId;
    }

    public int getId() { return id; }
    public String getText() { return text; }
    public String getUserId() { return userId; }
    public int getProductId() { return productId; }
}