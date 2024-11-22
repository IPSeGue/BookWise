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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private LinearLayout btnHome, btnSearch, btnCollection, btnGoal;
    private Button btnGoalSetting, btnSetting, btnLogOut;

    private TextView fullName, email, bookRead, bookGoal, currentProgress;
    private ImageView profileImage;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize RecyclerView
        initViews();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();

        loadUserData();

        btnHome.setOnClickListener(v -> homePage());
        btnSearch.setOnClickListener(v -> searchPage());
        btnCollection.setOnClickListener(v -> collectionPage());
        btnGoal.setOnClickListener(v -> goalPage());
        btnGoalSetting.setOnClickListener(v -> goalPage());
        btnSetting.setOnClickListener(v -> userSetting());
        btnLogOut.setOnClickListener(v -> logIn());
    }

    public void initViews(){
        btnHome = findViewById(R.id.h_home_btn);
        btnSearch = findViewById(R.id.h_search_btn);
        btnCollection = findViewById(R.id.h_collection_btn);
        btnGoal = findViewById(R.id.h_goal_btn);
        btnGoalSetting = findViewById(R.id.up_setGoal);
        btnSetting= findViewById(R.id.up_setting);
        btnLogOut = findViewById(R.id.up_logOut);

        fullName = findViewById(R.id.up_fullName);
        email = findViewById(R.id.up_email);
        profileImage = findViewById(R.id.up_image);
        bookRead = findViewById(R.id.up_bookRead);
        bookGoal = findViewById(R.id.up_bookGoal);
        currentProgress = findViewById(R.id.up_currentProgress);
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
                    Long userNumGoal = documentSnapshot.getLong("numGoal");
                    String timeFrame = documentSnapshot.getString("perTimeFrame");
                    Long userBookRead = documentSnapshot.getLong("numBookRead");
                    String imageUrl = documentSnapshot.getString("imageUrl"); // Assume profileImageUrl field

                    fullName.setText(name);
                    email.setText(userEmail);
                    bookRead.setText("Total Books Read: \n" + userBookRead);
                    bookGoal.setText("Reading Goal: \n" + userNumGoal + " Books \n" + timeFrame);
                    currentProgress.setText("Current Progress: \n" + userBookRead + " Books As \n of Now" );
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
                Toast.makeText(UserProfileActivity.this, "Error loading user data", Toast.LENGTH_SHORT).show();
            });
    }

    public void homePage(){
        Intent intent = new Intent(UserProfileActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void searchPage(){
        Intent intent = new Intent(UserProfileActivity.this, SearchActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void collectionPage(){
        Intent intent = new Intent(UserProfileActivity.this, CollectionActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void goalPage(){
        Intent intent = new Intent(UserProfileActivity.this, GoalSettingActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void userSetting(){
        Intent intent = new Intent(UserProfileActivity.this, UserSettingActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void logIn(){
        Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}