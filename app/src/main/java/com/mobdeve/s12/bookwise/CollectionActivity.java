package com.mobdeve.s12.bookwise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity implements HomeActivityAdapter.OnCollectClickListener{

    private LinearLayout btnHome, btnSearch, btnAdd, btnCollection, btnGoal;
    private ImageView userProfile;
    private RecyclerView rv_home_item;

    private HomeActivityAdapter activityHomeAdapter;
    private List<Bookitem> collectedItems;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userId = auth.getCurrentUser().getUid();
        checkUserCollection(userId);

        if (collectedItems == null) {
            collectedItems = new ArrayList<>(); // Initialize as empty if null
        }

        // Initialize RecyclerView
        initViews();

        btnHome.setOnClickListener(v -> homePage());
        btnSearch.setOnClickListener(v -> searchPage());
        btnAdd.setOnClickListener(v -> addPage());
        btnCollection.setOnClickListener(v -> collectionPage());
        btnGoal.setOnClickListener(v -> goalPage());
        userProfile.setOnClickListener(v -> userProfilePage());
    }

    private void checkUserCollection(String userId) {
        // Query Firestore for the user's personal book collection
        db.collection("users").document(userId).collection("bookCollection")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // User collection exists, populate bookitemList with user's books
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Bookitem book = document.toObject(Bookitem.class);
                            collectedItems.add(book);
                        }
                        activityHomeAdapter.notifyDataSetChanged(); // Update adapter with user's collection
                        Toast.makeText(this, "Loaded your book collection.", Toast.LENGTH_SHORT).show();
                    } else {
                        // No personal collection found; load default book list
                        loadDefaultBooks();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading books: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loadDefaultBooks() {
        // Load books from the DataGeneratorBooks singleton if no user collection exists
        collectedItems = new ArrayList<>();
        activityHomeAdapter = new HomeActivityAdapter(collectedItems, this, userId);
        rv_home_item.setAdapter(activityHomeAdapter);
        activityHomeAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Loaded default book list.", Toast.LENGTH_SHORT).show();
    }

    public void initViews(){
        btnHome = findViewById(R.id.h_home_btn);
        btnSearch = findViewById(R.id.h_search_btn);
        btnAdd = findViewById(R.id.h_add_btn);
        btnCollection = findViewById(R.id.h_collection_btn);
        btnGoal = findViewById(R.id.h_goal_btn);
        userProfile = findViewById(R.id.h_userProfile);
        rv_home_item = findViewById(R.id.rv_home_item);

        // Set Adapter
        rv_home_item.setLayoutManager(new LinearLayoutManager(this));
        activityHomeAdapter = new HomeActivityAdapter(collectedItems, this, userId);
        rv_home_item.setAdapter(activityHomeAdapter);
    }

    @Override
    public void onCollectClick(Bookitem item, boolean isCollected, String userId) {
        if (isCollected) {
            // Add book to Firestore
            db.collection("users").document(userId)
                    .collection("bookCollection").document(item.getBookId()) // Ensure item has a unique ID
                    .set(item)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(CollectionActivity.this, "Book added to collection", Toast.LENGTH_SHORT).show();
                        if (!collectedItems.contains(item)) {
                            collectedItems.add(item);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(CollectionActivity.this, "Failed to add book: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Remove book from Firestore
            db.collection("users").document(userId)
                    .collection("bookCollection").document(item.getBookId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(CollectionActivity.this, "Book removed from collection", Toast.LENGTH_SHORT).show();
                        collectedItems.remove(item);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(CollectionActivity.this, "Failed to remove book: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
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
        Intent intent = new Intent(CollectionActivity.this, AdvanceSearchActivity.class);
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