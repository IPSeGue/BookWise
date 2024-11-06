package com.mobdeve.s12.bookwise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

public class DataGeneratorBooks {
    private static DataGeneratorBooks instance;
    private FirebaseFirestore firestore;
    private CollectionReference booksCollection;
    private List<Bookitem> bookitemList = new ArrayList<>();
    private GoogleBookAPI googleBooksAPI;

    public DataGeneratorBooks(FirebaseFirestore firestore) {
        this.firestore = firestore; // "this" used to refer to the instance variable
        booksCollection = this.firestore.collection("books"); // or just firestore without 'this'
        googleBooksAPI = new GoogleBookAPI(); // no ambiguity, 'this' not needed
    }

    public void initializeBooks(String searchQuery, GoogleBookAPI.OnBooksFetchedListener listener) {
        checkFirestoreForData(searchQuery, listener);
    }

    private void checkFirestoreForData(String searchQuery, GoogleBookAPI.OnBooksFetchedListener listener) {
        booksCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                if (task.getResult().isEmpty()) {
                    // No data in Firestore, so fetch from Google Books API
                    fetchAndSaveBooks(searchQuery, listener);
                } else {
                    // Firestore has data, use existing data
                    try {
                        // Convert Firestore documents to Bookitem objects
                        List<Bookitem> books = task.getResult().toObjects(Bookitem.class);
                        if (books != null && !books.isEmpty()) {
                            listener.onBooksFetched(books); // Pass the existing data to the listener
                        } else {
                            listener.onError("No books found in Firestore.");
                        }
                    } catch (Exception e) {
                        // Handle any exceptions during the conversion process
                        listener.onError("Error parsing Firestore data: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Error checking Firestore: " + task.getException().getMessage());
                listener.onError("Error accessing Firestore data.");
            }
        });
    }

    public void fetchAndSaveBooks(String searchQuery, GoogleBookAPI.OnBooksFetchedListener listener) {
        googleBooksAPI.fetchBooks(searchQuery, new GoogleBookAPI.OnBooksFetchedListener() {
            @Override
            public void onBooksFetched(List<Bookitem> books) {
                listener.onBooksFetched(books); // Provide books to the listener
                saveBooks(books);  // Save books to Firestore
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
        for (Bookitem book : books) {
            HashMap<String, Object> bookData = new HashMap<>();
            bookData.put("title", book.getTitle());
            bookData.put("author", book.getAuthor());
            bookData.put("genres", book.getGenres());
            bookData.put("summary", book.getSummary());
            bookData.put("date", book.getDate());
            bookData.put("imageURL", book.getImageURL());

            // Add each book to Firestore with a unique document ID
            booksCollection.add(bookData).addOnSuccessListener(documentReference -> {
                System.out.println("Book saved with ID: " + documentReference.getId());
            }).addOnFailureListener(e -> {
                System.out.println("Error saving book: " + e.getMessage());
            });
        }
    }

    public static DataGeneratorBooks getInstance() {
        if (instance == null) {
            instance = new DataGeneratorBooks(FirebaseFirestore.getInstance());
        }
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
