package com.mobdeve.s12.bookwise;

import java.util.ArrayList;
import java.util.List;

public class DataGeneratorBooks {
    private static final DataGeneratorBooks instance = new DataGeneratorBooks();
    private List<Bookitem> bookitemList = new ArrayList<>();
    private GoogleBookAPI googleBooksAPI;

    public DataGeneratorBooks() {
        this.googleBooksAPI = new GoogleBookAPI();
    }

    // Method to fetch and save books
    public void fetchAndSaveBooks(String searchQuery, GoogleBookAPI.OnBooksFetchedListener listener) {
        googleBooksAPI.fetchBooks(searchQuery, new GoogleBookAPI.OnBooksFetchedListener() {
            @Override
            public void onBooksFetched(List<Bookitem> books) {
                listener.onBooksFetched(books);
                saveBooks(books);  // Optional: Save books to a database or other storage if needed.
            }

            @Override
            public void onError(String errorMessage) {
                listener.onError(errorMessage);
                System.out.println("Error fetching books: " + errorMessage);
            }
        });
    }

    // Method to save books (stub function to replace with actual save logic)
    private void saveBooks(List<Bookitem> books) {
        // For example, loop through and save each book (to a database or storage)
        for (Bookitem book : books) {
            System.out.println("Saving book: " + book.getTitle());
            // Add your save logic here, e.g., database insertion
        }
    }

    public static DataGeneratorBooks getInstance() {
        return instance;
    }

    public List<Bookitem> getBookList() {
        return bookitemList;
    }

    public void setBookList(List<Bookitem> bookList) {
        this.bookitemList = bookList;
    }

    public List<Bookitem> getCollectedBookitemList() {
        List<Bookitem> collectedBooks = new ArrayList<>();
        for (Bookitem item : bookitemList) {
            if (item.isCollected()) {
                collectedBooks.add(item);
            }
        }
        return collectedBooks;
    }
}
