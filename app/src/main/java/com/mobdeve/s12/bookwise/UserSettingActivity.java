package com.mobdeve.s12.bookwise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserSettingActivity extends AppCompatActivity {

    private LinearLayout btnHome, btnSearch, btnAdd, btnCollection, btnGoal;
    private Button btnSaveProfile;

    private TextView fullName, email;
    private ImageView profileImage;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        // Initialize RecyclerView
        initViews();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();

        loadUserData();

        btnHome.setOnClickListener(v -> homePage());
        btnSearch.setOnClickListener(v -> searchPage());
        btnAdd.setOnClickListener(v -> addPage());
        btnCollection.setOnClickListener(v -> collectionPage());
        btnGoal.setOnClickListener(v -> goalPage());
        btnSaveProfile.setOnClickListener(v -> userProfilePage());
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

                        fullName.setText(name);
                        email.setText(userEmail);
                        if (imageUrl != null && imageUrl.equals("default")){
                            profileImage.setImageResource(R.drawable.user_profile);
                        }
                        else{
                            // Load image using Glide if the image URL is available
                            Glide.with(this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.user_profile) // Optional: placeholder while loading
                                    .error(R.drawable.error_profile) // Optional: error image if URL fails
                                    .into(profileImage);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure, e.g., show a message
                    Toast.makeText(UserSettingActivity.this, "Error loading user data", Toast.LENGTH_SHORT).show();
                });
    }

    public void initViews(){
        btnHome = findViewById(R.id.h_home_btn);
        btnSearch = findViewById(R.id.h_search_btn);
        btnAdd = findViewById(R.id.h_add_btn);
        btnCollection = findViewById(R.id.h_collection_btn);
        btnGoal = findViewById(R.id.h_goal_btn);
        btnSaveProfile = findViewById(R.id.us_saveProfile);

        fullName = findViewById(R.id.us_fullName);
        email = findViewById(R.id.us_email);
        profileImage = findViewById(R.id.us_image);
    }


    public void homePage(){
        Intent intent = new Intent(UserSettingActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void searchPage(){
        Intent intent = new Intent(UserSettingActivity.this, SearchActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void addPage(){
        Intent intent = new Intent(UserSettingActivity.this, AdvanceSearchActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void collectionPage(){
        Intent intent = new Intent(UserSettingActivity.this, CollectionActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void goalPage(){
        Intent intent = new Intent(UserSettingActivity.this, GoalSettingActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void userProfilePage(){
        Intent intent = new Intent(UserSettingActivity.this, UserProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}