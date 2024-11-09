package com.mobdeve.s12.bookwise;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Bookitem implements Parcelable, Serializable {
    private String title, author, genres, summary, imageURL, bookId;
    private int rating, day, month, year;
    private boolean isCollected;
    private ArrayList<Reviews> reviews;

    public Bookitem() {
        // Empty constructor for Firestore
    }

    public Bookitem(String title, String author, String genres, String summary, int rating, int day, int month, int year, String imageURL) {
        this.title = title;
        this.author = author;
        this.genres = genres;
        this.summary = summary;
        this.rating = rating;
        this.day = day;
        this.month = month;
        this.year = year;
        this.imageURL = imageURL;
        this.isCollected = false;
        this.reviews = new ArrayList<>();
    }

    public String getBookId(){
        return bookId;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenres() {
        return genres;
    }

    public String getSummary() {
        return summary;
    }

    public int getRating() {
        return rating;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getImageURL(){
        return imageURL;
    }

    public String getDate() {
        return month + "/" + day + "/" + year;
    }

    public void setDocument(String documentId) {
        this.bookId = documentId;
    }

    public ArrayList<Reviews> getReviews() {
        return reviews;
    }

    public void addReview(Reviews review) {
        reviews.add(review);
        // Optionally, recalculate the book's average rating based on reviews
        calculateAverageRating();
    }

    private void calculateAverageRating() {
        int totalRating = 0;
        for (Reviews review : reviews) {
            totalRating += review.getRating();
        }
        this.rating = reviews.size() > 0 ? totalRating / reviews.size() : 0;
    }

    // Parcelable implementation
    protected Bookitem(Parcel in) {
        title = in.readString();
        author = in.readString();
        genres = in.readString();
        summary = in.readString();
        rating = in.readInt();
        day = in.readInt();
        month = in.readInt();
        year = in.readInt();
        imageURL = in.readString();
        isCollected = in.readByte() != 0;
    }

    public static final Creator<Bookitem> CREATOR = new Creator<Bookitem>() {
        @Override
        public Bookitem createFromParcel(Parcel in) {
            return new Bookitem(in);
        }

        @Override
        public Bookitem[] newArray(int size) {
            return new Bookitem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeString(genres);
        parcel.writeString(summary);
        parcel.writeInt(rating);
        parcel.writeInt(day);
        parcel.writeInt(month);
        parcel.writeInt(year);
        parcel.writeString(imageURL);
        parcel.writeByte((byte) (isCollected ? 1 : 0));
    }
}
