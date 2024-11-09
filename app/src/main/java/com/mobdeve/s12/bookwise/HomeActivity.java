package com.mobdeve.s12.bookwise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeActivityAdapter.OnCollectClickListener {

    private LinearLayout btnHome, btnSearch, btnAdd, btnCollection, btnGoal;
    private ImageView userProfile;
    private RecyclerView rv_home_item;

    private HomeActivityAdapter activityHomeAdapter;
    private List<Bookitem> bookitemList;
    private List<Bookitem> collectionBookitemList;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userId = auth.getCurrentUser().getUid();

        loadUserData();

        bookitemList = DataGeneratorBooks.getInstance().getBookList();
        if (bookitemList == null) {
            bookitemList = new ArrayList<>(); // Initialize as empty if null
        }

        collectionBookitemList = new ArrayList<>();

        // Initialize RecyclerView
        initViews();

        btnHome.setOnClickListener(v -> homePage());
        btnSearch.setOnClickListener(v -> searchPage());
        btnAdd.setOnClickListener(v -> addPage());
        btnCollection.setOnClickListener(v -> collectionPage());
        btnGoal.setOnClickListener(v -> goalPage());
        userProfile.setOnClickListener(v -> userProfilePage());
    }

    @Override
    public void onCollectClick(Bookitem item, boolean isCollected, String userId) {
        if (isCollected) {
            // Add book to Firestore
            db.collection("users").document(userId)
                    .collection("bookCollection").document(item.getBookId()) // Ensure item has a unique ID
                    .set(item)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(HomeActivity.this, "Book added to collection", Toast.LENGTH_SHORT).show();
                        if (!collectionBookitemList.contains(item)) {
                            collectionBookitemList.add(item);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(HomeActivity.this, "Failed to add book: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Remove book from Firestore
            db.collection("users").document(userId)
                    .collection("bookCollection").document(item.getBookId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(HomeActivity.this, "Book removed from collection", Toast.LENGTH_SHORT).show();
                        collectionBookitemList.remove(item);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(HomeActivity.this, "Failed to remove book: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void loadUserData() {
        // Reference to the Firestore user document
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Get and display the full name and email
                        String name = documentSnapshot.getString("fullName");
                        String userEmail = documentSnapshot.getString("email");
                        String imageUrl = documentSnapshot.getString("imageUrl"); // Assume profileImageUrl field

//                        fullName.setText(name);
//                        email.setText(userEmail);
                        if (imageUrl != null && imageUrl.equals("default")){
                            userProfile.setImageResource(R.drawable.user_profile);
                        }
                        else{
                            // Load image using Glide if the image URL is available
                            Glide.with(this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.user_profile) // Optional: placeholder while loading
                                    .error(R.drawable.error_profile) // Optional: error image if URL fails
                                    .into(userProfile);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure, e.g., show a message
                    Toast.makeText(HomeActivity.this, "Error loading user data", Toast.LENGTH_SHORT).show();
                });
    }

    public void initViews(){
        btnHome = findViewById(R.id.h_home_btn);
        btnSearch = findViewById(R.id.h_search_btn);
        btnAdd = findViewById(R.id.h_add_btn);
        btnCollection = findViewById(R.id.h_collection_btn);
        btnGoal = findViewById(R.id.h_goal_btn);
        userProfile = findViewById(R.id.h_userProfile);
        rv_home_item = findViewById(R.id.rv_home_item);

        rv_home_item.setLayoutManager(new LinearLayoutManager(this));
        activityHomeAdapter = new HomeActivityAdapter(bookitemList, this, userId);
        rv_home_item.setAdapter(activityHomeAdapter);
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