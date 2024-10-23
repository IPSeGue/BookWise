package com.mobdeve.s12.bookwise;

import java.util.List;

public class ApiBook {
    private VolumeInfo volumeInfo;

    public String getTitle() {
        return volumeInfo.title;
    }

    public String getAuthor() {
        return volumeInfo.authors != null && !volumeInfo.authors.isEmpty() ? volumeInfo.authors.get(0) : "Unknown Author";
    }

    public String getGenres() {
        return volumeInfo.categories != null && !volumeInfo.categories.isEmpty() ? volumeInfo.categories.get(0) : "Unknown Genre";
    }

    public String getSummary() {
        return volumeInfo.description != null ? volumeInfo.description : "No summary available";
    }

    public int getDay() {
        return volumeInfo.publishedDate != null ? extractDay(volumeInfo.publishedDate) : 0;
    }

    public int getMonth() {
        return volumeInfo.publishedDate != null ? extractMonth(volumeInfo.publishedDate) : 0;
    }

    public int getYear() {
        return volumeInfo.publishedDate != null ? extractYear(volumeInfo.publishedDate) : 0;
    }

    public String getImageURL() {
        return volumeInfo.imageLinks != null && volumeInfo.imageLinks.thumbnail != null ?
                volumeInfo.imageLinks.thumbnail : ""; // Return empty string if thumbnail not available
    }

    // Helper methods to extract the day, month, and year from the date string
    private int extractDay(String publishedDate) {
        // Parse day from publishedDate if available
        return 1; // Placeholder
    }

    private int extractMonth(String publishedDate) {
        // Parse month from publishedDate if available
        return 1; // Placeholder
    }

    private int extractYear(String publishedDate) {
        // Parse year from publishedDate if available
        return 2020; // Placeholder
    }

    private class VolumeInfo {
        String title;
        List<String> authors;
        String description;
        String publishedDate;
        List<String> categories;
        ImageLinks imageLinks;
    }

    private class ImageLinks {
        String thumbnail;
    }
}
