package com.mobdeve.s12.bookwise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnCollectClickListener {

    private LinearLayout btnHome, btnSearch, btnAdd, btnCollection, btnGoal;
    private ImageView userProfile;
    private RecyclerView rv_home_item;

    private HomeActivityAdapter activityHomeAdaptor;
    private List<Bookitem> bookitemList;
    private List<Bookitem> collectionBookitemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize RecyclerView
        initViews();
        rv_home_item.setLayoutManager(new LinearLayoutManager(this));

        // Initialize data
        bookitemList = new ArrayList<>();
        collectionBookitemList = new ArrayList<>();
        bookitemList.add(new Bookitem("Sum", "Name", "Roman", "This book is all about" ,5 , 14, 3,2020, R.drawable.google));
        bookitemList.add(new Bookitem("Minus", "Name", "Roman", "This book is all about" ,1 , 14, 3,2020, R.drawable.logo));
        bookitemList.add(new Bookitem("Multiply", "Name", "Roman", "This book is all about" ,3 , 14, 3,2020, R.drawable.x));

        // Set Adapter
        activityHomeAdaptor = new HomeActivityAdapter(bookitemList, this);
        rv_home_item.setAdapter(activityHomeAdaptor);


        btnHome.setOnClickListener(v -> homePage());
        btnSearch.setOnClickListener(v -> searchPage());
        btnAdd.setOnClickListener(v -> addPage());
        btnCollection.setOnClickListener(v -> collectionPage());
        btnGoal.setOnClickListener(v -> goalPage());
        userProfile.setOnClickListener(v -> userProfilePage());
    }

    public void onCollectClick(int position, boolean isCollected) {
        Bookitem item = bookitemList.get(position);
        item.setCollected(isCollected);  // Update the collected status in the model

        if (isCollected) {
            collectionBookitemList.add(item);  // Add to collected list
        } else {
            collectionBookitemList.remove(item);  // Remove from collected list
        }
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
        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void searchPage(){
        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void addPage(){
        Intent intent = new Intent(HomeActivity.this, AdvanceSearchActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void collectionPage(){
        Intent intent = new Intent(HomeActivity.this, CollectionActivity.class);
        intent.putParcelableArrayListExtra("collectedItems", new ArrayList<>(collectionBookitemList));
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void goalPage(){
        Intent intent = new Intent(HomeActivity.this, GoalSettingActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void userProfilePage(){
        Intent intent = new Intent(HomeActivity.this, UserProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}