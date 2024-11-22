package com.mobdeve.s12.bookwise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class AdvanceSearchActivity extends AppCompatActivity {

    private LinearLayout btnHome, btnSearch, btnAdd, btnCollection, btnGoal;
    private EditText etTitle, etAuthor, etGenre, etPublisher, etLanguage, etPublicationDate;
    private Button asSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_search);

        etTitle = findViewById(R.id.as_title);
        etAuthor = findViewById(R.id.as_author);
        etGenre = findViewById(R.id.as_genres);
        etPublisher = findViewById(R.id.as_publisher);
        etLanguage = findViewById(R.id.as_language);
        etPublicationDate = findViewById(R.id.as_publishDay);
        asSearch = findViewById(R.id.as_search);

        btnHome = findViewById(R.id.h_home_btn);
        btnSearch = findViewById(R.id.h_search_btn);
        btnAdd = findViewById(R.id.h_add_btn);
        btnCollection = findViewById(R.id.h_collection_btn);
        btnGoal = findViewById(R.id.h_goal_btn);

        asSearch.setOnClickListener(v -> {
            Map<String, String> searchFilters = new HashMap<>();
            // Collect search input
            addFilter(searchFilters, "title", etTitle.getText().toString().trim());
            addFilter(searchFilters, "author", etAuthor.getText().toString().trim());
            addFilter(searchFilters, "genre", etGenre.getText().toString().trim());
            addFilter(searchFilters, "publisher", etPublisher.getText().toString().trim());
            addFilter(searchFilters, "language", etLanguage.getText().toString().trim());
            addFilter(searchFilters, "publicationDate", etPublicationDate.getText().toString().trim());

            if (searchFilters.isEmpty()) {
                Toast.makeText(this, "Please enter at least one filter!", Toast.LENGTH_SHORT).show();
            } else {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("SEARCH_FILTERS", new HashMap<>(searchFilters));
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        btnHome.setOnClickListener(v -> homePage());
        btnSearch.setOnClickListener(v -> searchPage());
        btnAdd.setOnClickListener(v -> addPage());
        btnCollection.setOnClickListener(v -> collectionPage());
        btnGoal.setOnClickListener(v -> goalPage());
    }

    private void addFilter(Map<String, String> filters, String key, String value) {
        if (!value.isEmpty()) {
            filters.put(key, value);
        }
    }

    public void homePage() {
        Intent intent = new Intent(AdvanceSearchActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void searchPage() {
        Intent intent = new Intent(AdvanceSearchActivity.this, SearchActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void addPage() {
        Intent intent = new Intent(AdvanceSearchActivity.this, SearchActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void collectionPage() {
        Intent intent = new Intent(AdvanceSearchActivity.this, CollectionActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void goalPage() {
        Intent intent = new Intent(AdvanceSearchActivity.this, GoalSettingActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
