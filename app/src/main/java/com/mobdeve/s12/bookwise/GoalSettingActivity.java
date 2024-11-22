package com.mobdeve.s12.bookwise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GoalSettingActivity extends AppCompatActivity {

    private LinearLayout btnHome, btnSearch, btnCollection, btnGoal;
    private Button btnSaveGoal;

    private TextView fullName, email;
    private EditText numOfBooks;
    private Spinner timeFrame;
    private ImageView profileImage;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_setting);

        // Initialize RecyclerView
        initViews();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();

        loadTimeFrame();
        loadUserData();

        btnHome.setOnClickListener(v -> homePage());
        btnSearch.setOnClickListener(v -> searchPage());
        btnCollection.setOnClickListener(v -> collectionPage());
        btnGoal.setOnClickListener(v -> goalPage());
        btnSaveGoal.setOnClickListener(v -> saveGoals());

        timeFrame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected time frame value
                String selectedTimeFrame = parentView.getItemAtPosition(position).toString();
                // You can use the selectedTimeFrame value for your logic here
                Toast.makeText(GoalSettingActivity.this, "Selected: " + selectedTimeFrame, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle when nothing is selected, if needed
            }
        });
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
                        numOfBooks.setText(String.valueOf(userNumGoal));
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
                    Toast.makeText(GoalSettingActivity.this, "Error loading user data", Toast.LENGTH_SHORT).show();
                });
    }

    public void loadTimeFrame(){
        String[] timeFrames = {"Per Day", "Per Week", "Per Month", "Per Quarter", "Per Year"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeFrames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeFrame.setAdapter(adapter);
    }

    public void saveGoals() {
        // Get the values entered by the user
        int updateNumOfBook = Integer.parseInt(numOfBooks.getText().toString());
        String updateTimeFrame = timeFrame.getSelectedItem().toString();  // Correct way to get the selected value from Spinner

        if (!updateTimeFrame.isEmpty()) {
            // Reference to the Firestore user document
            db.collection("users").document(userId)
                    .update("numGoal", updateNumOfBook, "perTimeFrame", updateTimeFrame) // Update the user document with the new data
                    .addOnSuccessListener(aVoid -> {
                        // Show a success message
                        Toast.makeText(GoalSettingActivity.this, "Goals saved successfully!", Toast.LENGTH_SHORT).show();
                        userProfilePage();  // Navigate to the user profile page after saving
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure (optional)
                        Toast.makeText(GoalSettingActivity.this, "Failed to save goals. Try again.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Show a message if the number of books is empty
            Toast.makeText(GoalSettingActivity.this, "Please enter a number of books", Toast.LENGTH_SHORT).show();
        }
    }

    public void initViews(){
        btnHome = findViewById(R.id.h_home_btn);
        btnSearch = findViewById(R.id.h_search_btn);
        btnCollection = findViewById(R.id.h_collection_btn);
        btnGoal = findViewById(R.id.h_goal_btn);
        btnSaveGoal = findViewById(R.id.gs_saveGoal);

        fullName = findViewById(R.id.gs_fullName);
        email = findViewById(R.id.gs_email);
        profileImage = findViewById(R.id.gs_image);
        numOfBooks = findViewById(R.id.gs_number);
        timeFrame = findViewById(R.id.gs_timeFrame);
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

    public void collectionPage(){
        Intent intent = new Intent(GoalSettingActivity.this, CollectionActivity.class);
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