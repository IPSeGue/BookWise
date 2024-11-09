package com.mobdeve.s12.bookwise;

import java.util.ArrayList;

public class Users {
    private String fullName, email, password;
    private ArrayList<Bookitem> bookCollection;
    private ArrayList<Reviews> reviews;
    private String imageUrl;

    public Users(String fullName, String email, String password, String imageUrl){
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
        this.bookCollection = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    public String getFullName(){
        return fullName;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public ArrayList<Bookitem> getBooks() {
        return bookCollection;
    }

    public ArrayList<Reviews> getReviews() {
        return reviews;
    }

    public void addBookToCollection(Bookitem book) {
        bookCollection.add(book);
    }

    public void addReview(Reviews review) {
        reviews.add(review);
    }
}
