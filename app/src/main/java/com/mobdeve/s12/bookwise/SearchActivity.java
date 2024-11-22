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

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchActivityAdapter.OnCollectClickListener {

    private static final String TAG = "SearchActivity";
    private static final int ADVANCED_SEARCH_REQUEST = 1; // Request code for advanced search

    private LinearLayout btnHome, btnSearch, btnAdd, btnCollection, btnGoal, as_Main;
    private RecyclerView rv_search_item;
    private SearchView svSearch;
    private Button sAdvance;

    private SearchActivityAdapter activitySearchAdapter;
    private List<Bookitem> bookitemList;
    private List<Bookitem> collectionBookitemList;

    private GoogleBookAPI googleBookAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize Views and RecyclerView
        initViews();

        bookitemList = new ArrayList<>();
        collectionBookitemList = new ArrayList<>();

        rv_search_item.setLayoutManager(new LinearLayoutManager(this));
        activitySearchAdapter = new SearchActivityAdapter(bookitemList, this);
        rv_search_item.setAdapter(activitySearchAdapter);

        // Initialize GoogleBookAPI instance
        googleBookAPI = new GoogleBookAPI();

        // Set up SearchView
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBooks(query);
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
        btnAdd.setOnClickListener(v -> addPage());
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
            String query = data.getStringExtra("QUERY");
            if (query != null && !query.isEmpty()) {
                // Perform the search with the query
                searchBooks(query);
            }
        }
    }

    public void onCollectClick(Bookitem item, boolean isCollected) {
        if (isCollected) {
            if (!collectionBookitemList.contains(item)) {
                collectionBookitemList.add(item);
            }
        } else {
            collectionBookitemList.remove(item);
        }
    }

    public void initViews() {
        btnHome = findViewById(R.id.h_home_btn);
        btnSearch = findViewById(R.id.h_search_btn);
        btnAdd = findViewById(R.id.h_add_btn);
        btnCollection = findViewById(R.id.h_collection_btn);
        btnGoal = findViewById(R.id.h_goal_btn);
        rv_search_item = findViewById(R.id.rv_search_item);
        svSearch = findViewById(R.id.search_view); // Ensure your layout has a SearchView with this ID
        as_Main = findViewById(R.id.as_main);
    }

    private void searchBooks(String query) {
        googleBookAPI.fetchBooks(query, new GoogleBookAPI.OnBooksFetchedListener() {
            @Override
            public void onBooksFetched(List<Bookitem> books) {
                bookitemList.clear();
                bookitemList.addAll(books);
                activitySearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                Log.e(TAG, "Error fetching books: " + errorMessage);
                Toast.makeText(SearchActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
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

    public void addPage() {
        Intent intent = new Intent(SearchActivity.this, AdvanceSearchActivity.class);
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
