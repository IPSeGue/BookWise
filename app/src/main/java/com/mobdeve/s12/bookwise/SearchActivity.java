package com.mobdeve.s12.bookwise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchActivityAdapter.OnCollectClickListener {

    private LinearLayout btnHome, btnSearch, btnAdd, btnCollection, btnGoal;
    private RecyclerView rv_search_item;
    private Button btnAdvance;

    private SearchActivityAdapter activitySearchAdapter;
    private List<Bookitem> bookitemList;
    private List<Bookitem> collectionBookitemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize RecyclerView
        initViews();

        bookitemList = DataGeneratorBooks.getInstance().getBookList();

        if (bookitemList == null) {
            bookitemList = new ArrayList<>(); // Initialize as empty if null
        }

        rv_search_item.setLayoutManager(new LinearLayoutManager(this));

        // Initialize data
        collectionBookitemList = new ArrayList<>();

        // Set Adapter
        activitySearchAdapter = new SearchActivityAdapter(bookitemList, this);
        rv_search_item.setAdapter(activitySearchAdapter);


        btnHome.setOnClickListener(v -> homePage());
        btnSearch.setOnClickListener(v -> searchPage());
        btnAdd.setOnClickListener(v -> addPage());
        btnCollection.setOnClickListener(v -> collectionPage());
        btnGoal.setOnClickListener(v -> goalPage());
        btnAdvance.setOnClickListener(v -> advanceSearch());
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

    public void initViews(){
        btnHome = findViewById(R.id.h_home_btn);
        btnSearch = findViewById(R.id.h_search_btn);
        btnAdd = findViewById(R.id.h_add_btn);
        btnCollection = findViewById(R.id.h_collection_btn);
        btnGoal = findViewById(R.id.h_goal_btn);
        rv_search_item = findViewById(R.id.rv_search_item);
        btnAdvance = findViewById(R.id.s_advance);
    }

    public void homePage(){
        Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void searchPage(){
        Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void addPage(){
        Intent intent = new Intent(SearchActivity.this, AdvanceSearchActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void collectionPage(){
        Intent intent = new Intent(SearchActivity.this, CollectionActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void goalPage(){
        Intent intent = new Intent(SearchActivity.this, GoalSettingActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void advanceSearch(){
        Intent intent = new Intent(SearchActivity.this, AdvanceSearchActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}