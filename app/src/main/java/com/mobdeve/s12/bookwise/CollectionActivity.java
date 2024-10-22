package com.mobdeve.s12.bookwise;

import android.content.Intent;
import android.os.Bundle;
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

public class CollectionActivity extends AppCompatActivity {

    private LinearLayout btnHome, btnSearch, btnAdd, btnCollection, btnGoal;
    private ImageView userProfile;
    private RecyclerView rv_home_item;

    private HomeActivityAdapter activityHomeAdaptor;
    private ArrayList<Bookitem> collectedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        // Initialize RecyclerView
        initViews();
        rv_home_item.setLayoutManager(new LinearLayoutManager(this));


        collectedItems = getIntent().getParcelableArrayListExtra("collectedItems");


        // Set Adapter
        activityHomeAdaptor = new HomeActivityAdapter(collectedItems, null);
        rv_home_item.setAdapter(activityHomeAdaptor);


        btnHome.setOnClickListener(v -> homePage());
        btnSearch.setOnClickListener(v -> searchPage());
        btnAdd.setOnClickListener(v -> addPage());
        btnCollection.setOnClickListener(v -> collectionPage());
        btnGoal.setOnClickListener(v -> goalPage());
        userProfile.setOnClickListener(v -> userProfilePage());
    }
    public void initViews(){
        btnHome = findViewById(R.id.h_home_btn);
        btnSearch = findViewById(R.id.h_search_btn);
        btnAdd = findViewById(R.id.h_add_btn);
        btnCollection = findViewById(R.id.h_collection_btn);
        btnGoal = findViewById(R.id.h_goal_btn);
        userProfile = findViewById(R.id.h_userProfile);
        rv_home_item = findViewById(R.id.rv_home_item);
    }

    public void homePage(){
        Intent intent = new Intent(CollectionActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void searchPage(){
        Intent intent = new Intent(CollectionActivity.this, SearchActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void addPage(){
        Intent intent = new Intent(CollectionActivity.this, ForgotPassActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void collectionPage(){
        Intent intent = new Intent(CollectionActivity.this, CollectionActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void goalPage(){
        Intent intent = new Intent(CollectionActivity.this, GoalSettingActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void userProfilePage(){
        Intent intent = new Intent(CollectionActivity.this, UserProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}