package com.mobdeve.s12.bookwise;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Button btnRegister;
    private TextView  btnLogin;
    private EditText fullName, emailInput, passWord, confirmPass;
    private ImageButton facebook, goolge, x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews();
        btnRegister.setOnClickListener(v -> register());
        btnLogin.setOnClickListener(v -> signUpPage());
    }

    public void initViews(){
        btnRegister = findViewById(R.id.su_register);
        fullName = findViewById(R.id.su_fullName);
        emailInput = findViewById(R.id.su_email);
        passWord = findViewById(R.id.su_passWord);
        confirmPass = findViewById(R.id.su_confirmPass);
        btnLogin = findViewById(R.id.su_logIn);
        facebook = findViewById(R.id.su_facebook);
        goolge = findViewById(R.id.su_google);
        x = findViewById(R.id.su_x);
    }

    public void register() {
        String re_fullName = fullName.getText().toString().trim();
        String re_email = emailInput.getText().toString().trim();
        String re_password = passWord.getText().toString().trim();
        String re_confirmPassword = confirmPass.getText().toString().trim();

        if (isInputValid(re_fullName, re_email, re_password, re_confirmPassword)) {
            // Check if the email is already in Firestore
            db.collection("users").whereEqualTo("email", re_email)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                // No existing user, proceed with registration
                                mAuth.createUserWithEmailAndPassword(re_email, re_password)
                                        .addOnCompleteListener(authTask -> {
                                            if (authTask.isSuccessful()) {
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                saveUserDataToFirestore(user, re_fullName, re_email, re_password);
                                            } else {
                                                Toast.makeText(SignUpActivity.this, "Registration Failed: " + authTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                // User with this email already exists
                                Toast.makeText(SignUpActivity.this, "This email is already registered.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this, "Error checking user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void saveUserDataToFirestore(FirebaseUser user, String fullName, String email, String password) {
        // Create a user data map
        List<Map<String, Object>> bookCollection = new ArrayList<>();
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("fullName", fullName);
        userData.put("email", email);
        String hashPassword= BCrypt.hashpw(password, BCrypt.gensalt());
        userData.put("password", hashPassword);
        userData.put("verifyPassword", password);
        userData.put("imageUrl", "default");
        userData.put("numBookRead", 0);
        userData.put("numGoal", 0);
        userData.put("perTimeFrame", "");

        // Save to Firestore
        db.collection("users").document(user.getUid())
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SignUpActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SignUpActivity.this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public boolean isInputValid(String re_fullName, String re_email, String re_password, String re_confirmPassword){
        List<String> errorMessages = new ArrayList<>();
        boolean isValid = true;

        if (re_fullName.isEmpty()) {
            errorMessages.add("Full Name cannot be empty");
            isValid = false;
        }

        if (re_email.isEmpty()) {
            errorMessages.add("Email cannot be empty");
            isValid = false;
        }

        if (re_password.isEmpty() || re_password.length() < 2) {
            errorMessages.add("Your password is too short. Try Again!");
            isValid = false;
        }

        if (re_confirmPassword.isEmpty() || !re_password.equals(re_confirmPassword)) {
            errorMessages.add("Passwords do not match");
            isValid = false;
        }

        if (!isValid) {
            showErrorDialog(errorMessages);
        }

        return isValid;
    }

    private void showErrorDialog(List<String> errorMessages) {
        StringBuilder message = new StringBuilder("Please fix the following issues:\n");
        for (String errorMessage : errorMessages) {
            message.append("- ").append(errorMessage).append("\n");
        }

        new AlertDialog.Builder(this)
                .setTitle("Input Errors")
                .setMessage(message.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    public void signUpPage(){
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}