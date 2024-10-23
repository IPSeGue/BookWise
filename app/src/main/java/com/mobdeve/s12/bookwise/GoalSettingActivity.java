package com.mobdeve.s12.bookwise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class GoalSettingActivity extends AppCompatActivity {

    private LinearLayout btnHome, btnSearch, btnAdd, btnCollection, btnGoal;
    private Button btnSaveGoal;

    private List<Bookitem> collectionBookitemList;

    //private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_setting);

        // Initialize RecyclerView
        initViews();
        collectionBookitemList = new ArrayList<>();



        btnHome.setOnClickListener(v -> homePage());
        btnSearch.setOnClickListener(v -> searchPage());
        btnAdd.setOnClickListener(v -> addPage());
        btnCollection.setOnClickListener(v -> collectionPage());
        btnGoal.setOnClickListener(v -> goalPage());
        btnSaveGoal.setOnClickListener(v -> userProfilePage());
    }

    public void initViews(){
        btnHome = findViewById(R.id.h_home_btn);
        btnSearch = findViewById(R.id.h_search_btn);
        btnAdd = findViewById(R.id.h_add_btn);
        btnCollection = findViewById(R.id.h_collection_btn);
        btnGoal = findViewById(R.id.h_goal_btn);
        btnSaveGoal = findViewById(R.id.gs_saveGoal);
    }

    public void homePage(){
        Intent intent = new Intent(GoalSettingActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void searchPage(){
        Intent intent = new Intent(GoalSettingActivity.this, SearchActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void addPage(){
        Intent intent = new Intent(GoalSettingActivity.this, AdvanceSearchActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void collectionPage(){
        Intent intent = new Intent(GoalSettingActivity.this, CollectionActivity.class);
        intent.putParcelableArrayListExtra("collectedItems", new ArrayList<>(collectionBookitemList));
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void goalPage(){
        Intent intent = new Intent(GoalSettingActivity.this, GoalSettingActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void userProfilePage(){
        Intent intent = new Intent(GoalSettingActivity.this, UserProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}