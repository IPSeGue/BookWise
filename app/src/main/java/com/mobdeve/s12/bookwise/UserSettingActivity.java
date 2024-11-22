package com.mobdeve.s12.bookwise;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UserSettingActivity extends AppCompatActivity {

    private LinearLayout btnHome, btnSearch, btnCollection, btnGoal;
    private Button btnSaveProfile;

    private TextView bookRead, bookGoal, currentProgress;
    private EditText fullName, email;
    private ImageView profileImage;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    String userId;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

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
        btnCollection.setOnClickListener(v -> collectionPage());
        btnGoal.setOnClickListener(v -> goalPage());
        btnSaveProfile.setOnClickListener(v -> saveProfileChanges());

        profileImage.setOnClickListener(v ->changeProfile());
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
                    Toast.makeText(UserSettingActivity.this, "Error loading user data", Toast.LENGTH_SHORT).show();
                });
    }

    public void initViews(){
        btnHome = findViewById(R.id.h_home_btn);
        btnSearch = findViewById(R.id.h_search_btn);
        btnCollection = findViewById(R.id.h_collection_btn);
        btnGoal = findViewById(R.id.h_goal_btn);
        btnSaveProfile = findViewById(R.id.us_saveProfile);

        fullName = findViewById(R.id.us_fullName);
        email = findViewById(R.id.us_email);
        profileImage = findViewById(R.id.us_image);
        bookRead = findViewById(R.id.us_bookRead);
        bookGoal = findViewById(R.id.us_bookGoal);
        currentProgress = findViewById(R.id.us_currentProgress);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData(); // Store the selected image URI
            Glide.with(this).load(imageUri).into(profileImage); // Display selected image
        }
    }

    private void changeProfile() {
        // Open the image picker when profileImage is clicked
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void saveProfileChanges() {
        String updatedFullName = fullName.getText().toString();
        String updatedEmail = email.getText().toString();

        // Step 1: Check if the email already exists in Firestore
        db.collection("users")
                .whereEqualTo("email", updatedEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty() && !updatedEmail.equals(auth.getCurrentUser().getEmail())) {
                        // Email already exists and is not the current user's email
                        Toast.makeText(this, "Email is already in use. Please choose another.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Step 2: Email is unique or it's the current user's email, so proceed with updates
                        updateUserProfile(updatedFullName, updatedEmail);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore Error", "Error checking email", e);
                    Toast.makeText(this, "Failed to check email. Try again.", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUserProfile(String updatedFullName, String updatedEmail) {
        // Step 3: Update full name and email in Firestore and Firebase Authentication
        db.collection("users").document(userId)
                .update("fullName", updatedFullName)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Full name updated successfully"))
                .addOnFailureListener(e -> Log.e("Firestore Error", "Error updating full name", e));

        // Update email in Firebase Authentication
        auth.getCurrentUser().updateEmail(updatedEmail)
                .addOnSuccessListener(aVoid -> Log.d("Auth", "Email updated successfully"))
                .addOnFailureListener(e -> {
                    Log.e("Auth Error", "Error updating email", e);
                    Toast.makeText(this, "Failed to update email. Try again.", Toast.LENGTH_SHORT).show();
                });

        // Step 4: Check if an image URI is selected, then update it in Firestore
        if (imageUri != null) {
            db.collection("users").document(userId)
                    .update("imageUrl", imageUri.toString())
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firestore", "Image URL updated successfully");
                        Toast.makeText(this, "Profile image updated successfully", Toast.LENGTH_SHORT).show();
                        // Navigate to userProfilePage once all updates are done
                        userProfilePage();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore Error", "Error updating image URL", e);
                        Toast.makeText(this, "Failed to update image URL", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.d("Image URI", "No image selected");
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
        }
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

// verify the email first to udpate the email;
/*FirebaseUser user = auth.getCurrentUser();

// Step 1: Send verification email if user is logged in
if (user != null && !user.isEmailVerified()) {
        user.sendEmailVerification()
        .addOnSuccessListener(aVoid -> {
        Log.d("Auth", "Verification email sent.");
            Toast.makeText(this, "A verification email has been sent. Please check your inbox.", Toast.LENGTH_SHORT).show();
        })
                .addOnFailureListener(e -> {
        Log.e("Auth Error", "Error sending verification email", e);
            Toast.makeText(this, "Failed to send verification email. Try again.", Toast.LENGTH_SHORT).show();
        });
                }

// Step 2: Proceed with updating the email once the verification is complete
                if (user.isEmailVerified()) {
        user.updateEmail(updatedEmail)
        .addOnSuccessListener(aVoid -> {
        Log.d("Auth", "Email updated successfully");
// Update email in Firestore as well
            db.collection("users").document(user.getUid())
        .update("email", updatedEmail)
                .addOnSuccessListener(aVoid1 -> {
        Log.d("Firestore", "Email updated successfully in Firestore");
                })
                        .addOnFailureListener(e -> {
        Log.e("Firestore Error", "Error updating email in Firestore", e);
                });
                        })
                        .addOnFailureListener(e -> {
        Log.e("Auth Error", "Error updating email", e);
            Toast.makeText(this, "Failed to update email. Try again.", Toast.LENGTH_SHORT).show();
        });
                } else {
                // Let the user know they need to verify their email before updating
                Toast.makeText(this, "Please verify your email before updating.", Toast.LENGTH_SHORT).show();
}*/
