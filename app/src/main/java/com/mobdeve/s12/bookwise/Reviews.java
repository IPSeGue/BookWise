package com.mobdeve.s12.bookwise;

public class Reviews {
    private String userId;
    private String bookId;
    private String content;
    private float rating;

    public Reviews() {
        // Empty constructor for Firestore
    }

    public Reviews(String userId, String bookId, String content, float rating) {
        this.userId = userId;
        this.bookId = bookId;
        this.content = content;
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
