package com.mobdeve.s12.bookwise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
            String title = etTitle.getText().toString().trim();
            String author = etAuthor.getText().toString().trim();
            String genre = etGenre.getText().toString().trim();
            String publisher = etPublisher.getText().toString().trim();
            String language = etLanguage.getText().toString().trim();
            String publicationDate = etPublicationDate.getText().toString().trim();

            // Build the query string
            StringBuilder queryBuilder = new StringBuilder();
            if (!title.isEmpty()) queryBuilder.append("+intitle:").append(title);
            if (!author.isEmpty()) queryBuilder.append("+inauthor:").append(author);
            if (!genre.isEmpty()) queryBuilder.append("+subject:").append(genre);
            if (!publisher.isEmpty()) queryBuilder.append("+inpublisher:").append(publisher);
            if (!language.isEmpty()) queryBuilder.append("+lang:").append(language);
            if (!publicationDate.isEmpty()) queryBuilder.append("+inpublisher:").append(publicationDate);

            String query = queryBuilder.toString();
            if (query.isEmpty()) {
                Toast.makeText(this, "Please enter at least one filter!", Toast.LENGTH_SHORT).show();
            } else {
                // Pass the query back to SearchActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("QUERY", query);
                setResult(RESULT_OK, resultIntent);
                finish(); // Close AdvanceSearchActivity
            }
        });

        btnHome.setOnClickListener(v -> homePage());
        btnSearch.setOnClickListener(v -> searchPage());
        btnAdd.setOnClickListener(v -> addPage());
        btnCollection.setOnClickListener(v -> collectionPage());
        btnGoal.setOnClickListener(v -> goalPage());
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
        Intent intent = new Intent(AdvanceSearchActivity.this, AdvanceSearchActivity.class);
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
