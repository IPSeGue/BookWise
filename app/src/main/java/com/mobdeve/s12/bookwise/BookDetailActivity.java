package com.mobdeve.s12.bookwise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {
    private LinearLayout btnHome, btnSearch, btnAdd, btnCollection, btnGoal;
    private Button btnSubmit;
    private RecyclerView rv_review_item;

    private ImageView bookImageID;
    private TextView bookTitle, bookAuthor, bookSummary, bookGenres, bookDate;


    private HomeActivityAdapter activityHomeAdaptor;
    private List<Bookitem> bookitemList;
    private List<Bookitem> collectionBookitemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Initialize RecyclerView
        initViews();
        rv_review_item.setLayoutManager(new LinearLayoutManager(this));
        data();

//        // Initialize data
//        bookitemList = new ArrayList<>();
//        collectionBookitemList = new ArrayList<>();
//        bookitemList.add(new Bookitem("Sum", "Name", "Roman", "This book is all about" ,5 , 14, 3,2020, R.drawable.google));
//        bookitemList.add(new Bookitem("Minus", "Name", "Roman", "This book is all about" ,1 , 14, 3,2020, R.drawable.logo));
//        bookitemList.add(new Bookitem("Multiply", "Name", "Roman", "This book is all about" ,3 , 14, 3,2020, R.drawable.x));
//
//        // Set Adapter
//        activityHomeAdaptor = new HomeActivityAdapter(bookitemList, this);
//        rv_review_item.setAdapter(activityHomeAdaptor);



        btnHome.setOnClickListener(v -> homePage());
        btnSearch.setOnClickListener(v -> searchPage());
        btnAdd.setOnClickListener(v -> addPage());
        btnCollection.setOnClickListener(v -> collectionPage());
        btnGoal.setOnClickListener(v -> goalPage());
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
        btnSubmit = findViewById(R.id.bd_submit);
        rv_review_item = findViewById(R.id.rv_review_item);

        bookImageID = findViewById(R.id.bd_imageID);
        bookTitle = findViewById(R.id.bd_title);
        bookAuthor = findViewById(R.id.bd_author);
        bookGenres = findViewById(R.id.bd_place);
        bookDate = findViewById(R.id.bd_date);
        bookSummary = findViewById(R.id.bd_summary);
    }

    public void data(){
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String genres = intent.getStringExtra("genres");
        String summary = intent.getStringExtra("summary");
        String date = intent.getStringExtra("date");
        String imageURL = intent.getStringExtra("imageURL");

        bookTitle.setText(title);
        bookAuthor.setText("By: "+ author);
        bookGenres.setText(genres);
        bookSummary.setText(summary);
        bookDate.setText(date);
        Glide.with(this)
                .load(imageURL)  // Use the image URL retrieved from the intent
                .placeholder(R.drawable.google)  // Optional: a placeholder while the image loads
                .error(R.drawable.logo)  // Optional: an error image if loading fails
                .into(bookImageID);
    }

    public void homePage(){
        Intent intent = new Intent(BookDetailActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void searchPage(){
        Intent intent = new Intent(BookDetailActivity.this, SearchActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void addPage(){
        Intent intent = new Intent(BookDetailActivity.this, AdvanceSearchActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void collectionPage(){
        Intent intent = new Intent(BookDetailActivity.this, CollectionActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void goalPage(){
        Intent intent = new Intent(BookDetailActivity.this, GoalSettingActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}