package com.mobdeve.s12.bookwise;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements SearchActivityAdapter.OnCollectClickListener {

    private static final String TAG = "SearchActivity";
    private static final int ADVANCED_SEARCH_REQUEST = 1; // Request code for advanced search

    private LinearLayout btnHome, btnSearch, btnCollection, btnGoal;
    private RecyclerView rv_search_item;
    private SearchView svSearch;
    private Button sAdvance;

    private SearchActivityAdapter activitySearchAdapter;
    private List<Bookitem> bookitemList;
    private List<Bookitem> collectionBookitemList;
    private List<Bookitem> filteredList;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userId = auth.getCurrentUser().getUid();

        // Initialize Views and RecyclerView
        initViews();

        //bookitemList = new ArrayList<>(); //original
        bookitemList = DataGeneratorBooks.getInstance().getBookList(); //edit
        filteredList = new ArrayList<>();
        collectionBookitemList = new ArrayList<>();


        rv_search_item.setLayoutManager(new LinearLayoutManager(this));
        activitySearchAdapter = new SearchActivityAdapter(filteredList, this, userId);
        rv_search_item.setAdapter(activitySearchAdapter);

        // Set up SearchView
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBooks(query, bookitemList);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optional: Real-time search as the user types
                return false;
            }
        });

        // Navigation buttons
        btnHome.setOnClickListener(v -> homePage());
        btnSearch.setOnClickListener(v -> searchPage());
        btnCollection.setOnClickListener(v -> collectionPage());
        btnGoal.setOnClickListener(v -> goalPage());

        // Set click listener for the sAdvance button (Advanced Search)
        sAdvance = findViewById(R.id.s_advance);

        sAdvance.setOnClickListener(v -> {
            // Open AdvanceSearchActivity to allow the user to input filters
            Intent intent = new Intent(SearchActivity.this, AdvanceSearchActivity.class);
            startActivityForResult(intent, ADVANCED_SEARCH_REQUEST);
        });
    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == ADVANCED_SEARCH_REQUEST && resultCode == RESULT_OK) {
                // Retrieve the search query from the result
                HashMap<String, String> searchFilters = (HashMap<String, String>) data.getSerializableExtra("SEARCH_FILTERS");
                if (searchFilters != null && !searchFilters.isEmpty()) {
                    // Perform the search with the query
                    advanceSearchBooks(searchFilters, bookitemList);
                }
            }
        }

    public void onCollectClick(Bookitem item, boolean isCollected, String userId) {
        if (isCollected) {
            // Add book to Firestore
            db.collection("users").document(userId)
                    .collection("bookCollection").document(item.getBookId()) // Ensure item has a unique ID
                    .set(item)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(SearchActivity.this, "Book added to collection", Toast.LENGTH_SHORT).show();
                        if (!collectionBookitemList.contains(item)) {
                            collectionBookitemList.add(item);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(SearchActivity.this, "Failed to add book: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Remove book from Firestore
            db.collection("users").document(userId)
                    .collection("bookCollection").document(item.getBookId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(SearchActivity.this, "Book removed from collection", Toast.LENGTH_SHORT).show();
                        collectionBookitemList.remove(item);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(SearchActivity.this, "Failed to remove book: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    public void initViews() {
        btnHome = findViewById(R.id.h_home_btn);
        btnSearch = findViewById(R.id.h_search_btn);
        btnCollection = findViewById(R.id.h_collection_btn);
        btnGoal = findViewById(R.id.h_goal_btn);
        rv_search_item = findViewById(R.id.rv_search_item);
        svSearch = findViewById(R.id.search_view);
    }

    private void searchBooks(String query, List<Bookitem> bookitemList) {
        // Iterate through the bookitemList to find matches
        filteredList.clear();
        for (Bookitem book : bookitemList) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(book);
            }
        }

        // Update the adapter with the filtered list
        activitySearchAdapter.notifyDataSetChanged();

        // Show a message if no results are found
        if (filteredList.isEmpty()) {
            Toast.makeText(SearchActivity.this, "No books found matching your query.", Toast.LENGTH_SHORT).show();
        }
    }

    private void advanceSearchBooks(Map<String, String> searchFilters, List<Bookitem> bookitemList) {
        filteredList.clear();

        for (Bookitem book : bookitemList) {
            boolean matches = true;

            for (Map.Entry<String, String> filter : searchFilters.entrySet()) {
                String key = filter.getKey();
                String value = filter.getValue().toLowerCase();

                // Match against corresponding Bookitem attributes
                switch (key) {
                    case "title":
                        matches &= (book.getTitle() != null && book.getTitle().toLowerCase().contains(value));
                        break;
                    case "author":
                        matches &= (book.getAuthor() != null && book.getAuthor().toLowerCase().contains(value));
                        break;
                    case "genre":
                        matches &= (book.getGenres() != null && book.getGenres().toLowerCase().contains(value));
                        break;
                    case "publisher":
                        matches &= (book.getAuthor() != null && book.getAuthor().toLowerCase().contains(value));
                        break;
                    case "language":
                        matches &= (book.getTitle() != null && book.getTitle().toLowerCase().contains(value));
                        break;
                    case "publicationDate":
                        matches &= (book.getDate() != null && book.getDate().toLowerCase().contains(value));
                        break;
                    default:
                        matches = false; // Unknown filter key
                }

                // Break early if any filter doesn't match
                if (!matches) break;
            }

            if (matches) {
                filteredList.add(book);
            }
        }

        activitySearchAdapter.notifyDataSetChanged();

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No books found matching the criteria", Toast.LENGTH_SHORT).show();
        }
    }


    public void homePage() {
        Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void searchPage() {
        Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void collectionPage() {
        Intent intent = new Intent(SearchActivity.this, CollectionActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void goalPage() {
        Intent intent = new Intent(SearchActivity.this, GoalSettingActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
