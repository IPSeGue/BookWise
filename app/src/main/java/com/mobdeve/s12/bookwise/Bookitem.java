package com.mobdeve.s12.bookwise;

public class Bookitem {
    private String title, author, genres, summary;
    private int rating, day, month, year, imageID;
    //private Review review;

    public Bookitem(String title, String author, String genres, String summary, int rating, int day, int month, int year) {
        this.title = title;
        this.author = author;
        this.genres = genres;
        this.summary = summary;
        this.rating = rating;
        this.day = day;
        this.month = month;
        this.year = year;
        this.imageID = R.drawable.logo;
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

    public int getImageID(){ return imageID; }

    public String getDate() {
        return month + "/" + day + "/" + year;
    }

}
