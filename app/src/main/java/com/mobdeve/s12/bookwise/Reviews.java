package com.mobdeve.s12.bookwise;

public class Reviews {
    private int id;
    private Users user;
    private Bookitem book;
    private String review;
    private int rating;

    public Reviews(int id, Users user, Bookitem book, String review, int rating) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.review = review;
        this.rating = rating;
    }

    public Users getUser(){
        return user;
    }



    public String getComment(){
        return review;
    }

    public int getRating(){
        return rating;
    }
}
