package com.mobdeve.s12.bookwise;

import android.os.Parcel;
import android.os.Parcelable;

public class Bookitem implements Parcelable {
    private String title, author, genres, summary;
    private int rating, day, month, year, imageID;
    private boolean isCollected;
    //private Review review;

    public Bookitem(String title, String author, String genres, String summary, int rating, int day, int month, int year, int imageID) {
        this.title = title;
        this.author = author;
        this.genres = genres;
        this.summary = summary;
        this.rating = rating;
        this.day = day;
        this.month = month;
        this.year = year;
        this.imageID = imageID;
        this.isCollected = false;
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

    public int getImageID(){ return imageID; }

    public String getDate() {
        return month + "/" + day + "/" + year;
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
        imageID = in.readInt();
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
        parcel.writeInt(imageID);
        parcel.writeByte((byte) (isCollected ? 1 : 0));
    }
}
