package com.mobdeve.s12.bookwise;

import java.util.ArrayList;

public class Users {
    private String fullName, email, password;
    private ArrayList<Bookitem> bookCollection;
    private ArrayList<Reviews> reviews;
    private int imageID;

    public Users(String fullName, String email, String password, int imageID){
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.imageID = imageID;
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

    public int getImageID(){
        return imageID;
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
