package com.mobdeve.s12.bookwise;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdvanceSearchActivity extends AppCompatActivity {

    private EditText etAuthor, etGenre, etPublicationDate;
    private Button btnAdvance;
    private RecyclerView rvAdvancedSearchResults;

    private SearchActivityAdapter activitySearchAdapter;
    private List<Bookitem> bookitemList;

    private GoogleBookAPI googleBookAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_search);

        initViews();

        bookitemList = new ArrayList<>();
        activitySearchAdapter = new SearchActivityAdapter(bookitemList, this::onCollectClick);

        // Set RecyclerView LayoutManager and Adapter after initialization
        rvAdvancedSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvAdvancedSearchResults.setAdapter(activitySearchAdapter);

        googleBookAPI = new GoogleBookAPI();

        // Add log to check if button is being clicked
        btnAdvance.setOnClickListener(v -> {
            Log.d("AdvanceSearch", "Advance Search button clicked!");

            String author = etAuthor.getText().toString().trim();
            String genre = etGenre.getText().toString().trim();
            String publicationDate = etPublicationDate.getText().toString().trim();

            StringBuilder queryBuilder = new StringBuilder();
            if (!author.isEmpty()) {
                queryBuilder.append("+inauthor:").append(author);
            }
            if (!genre.isEmpty()) {
                queryBuilder.append("+subject:").append(genre);
            }
            if (!publicationDate.isEmpty()) {
                queryBuilder.append("+inpublisher:").append(publicationDate);
            }

            String query = queryBuilder.toString();
            Log.d("AdvanceSearch", "Constructed query: " + query);

            if (query.isEmpty()) {
                Toast.makeText(this, "Please enter at least one filter!", Toast.LENGTH_SHORT).show();
                Log.d("AdvanceSearch", "Query is empty!");
            } else {
                Log.d("AdvanceSearch", "Performing advanced search...");
                performAdvancedSearch(query);
            }
        });
    }

    private void performAdvancedSearch(String query) {
        Log.d("AdvanceSearch", "Performing advanced search with query: " + query);

        googleBookAPI.fetchBooks(query, new GoogleBookAPI.OnBooksFetchedListener() {
            @Override
            public void onBooksFetched(List<Bookitem> books) {
                Log.d("AdvanceSearch", "Books fetched: " + books.size());

                if (books.isEmpty()) {
                    Toast.makeText(AdvanceSearchActivity.this, "No results found!", Toast.LENGTH_SHORT).show();
                    Log.d("AdvanceSearch", "No books found!");
                } else {
                    bookitemList.clear();
                    bookitemList.addAll(books);
                    activitySearchAdapter.notifyDataSetChanged();
                    Log.d("AdvanceSearch", "Books added to the list.");
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("AdvanceSearch", "Error: " + errorMessage);
                Toast.makeText(AdvanceSearchActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        etAuthor = findViewById(R.id.as_author);
        etGenre = findViewById(R.id.as_genres);
        etPublicationDate = findViewById(R.id.as_publishDay);
        btnAdvance = findViewById(R.id.s_advance);
        rvAdvancedSearchResults = findViewById(R.id.rv_search_item);  // Correct ID used here
    }

    private void onCollectClick(Bookitem bookitem, boolean isCollected) {
        // Handle "add to collection" logic if needed
    }
}
