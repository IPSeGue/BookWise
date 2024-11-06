package com.mobdeve.s12.bookwise;

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
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogIn;
    private TextView btnSignUp, btnForgotPass;
    private EditText emailInput, passWord;
    private ImageButton facebook, goolge, x;
    //private List<Bookitem> books;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        FirebaseApp.initializeApp(this);
        fetchBooksAndProceed();

        btnLogIn.setOnClickListener(v -> logIn());
        btnSignUp.setOnClickListener(v -> signUpPage());
        btnForgotPass.setOnClickListener(v -> forgotPassPage());

        emailInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailInput.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(emailInput, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
    }
    public void initViews(){
        btnLogIn = findViewById(R.id.l_logIn);
        btnSignUp = findViewById(R.id.l_signUp);
        btnForgotPass = findViewById(R.id.l_forgotPass);
        emailInput = findViewById(R.id.l_email_Input);
        passWord = findViewById(R.id.l_passWord);
        facebook = findViewById(R.id.l_facebook);
        goolge = findViewById(R.id.l_google);
        x = findViewById(R.id.l_x);
    }

    private void fetchBooksAndProceed() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DataGeneratorBooks dataGenerator = new DataGeneratorBooks(db);
        dataGenerator.initializeBooks("fiction", new GoogleBookAPI.OnBooksFetchedListener() {
            @Override
            public void onBooksFetched(List<Bookitem> books) {
                // Set the fetched books in the singleton instance
                DataGeneratorBooks.getInstance().setBookList(books);
                Toast.makeText(LoginActivity.this, "Books fetched successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error if fetching books fails
                Toast.makeText(LoginActivity.this, "Error fetching books: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void logIn(){
        String email = emailInput.getText().toString().trim();
        String password = passWord.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
        } else {
            // Replace with your actual credential check logic
            checkCredentials(email,password);

//                        if (rememberMeCheckbox.isChecked()) {
//                            saveRememberMe(email, password);
//                        } else {
//                            clearRememberMe();
//                        }
        }
    }

    private void checkCredentials(String email, String password) {
        // Get Firestore instance
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Query the users collection for the email
        firestore.collection("users")
                .whereEqualTo("email", email) // Match the email
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        // Check if we found a matching user
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        String storedHashedPassword = document.getString("password"); // The hashed password

                        // Check if the entered password matches the stored hashed password
                        if (storedHashedPassword != null && BCrypt.checkpw(password, storedHashedPassword)) {
                            // Password is correct, proceed with login
                            Toast.makeText(LoginActivity.this, "Sign-In Successful", Toast.LENGTH_SHORT).show();
                            // Call method to load the next activity
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);  // Start the HomeActivity
                            finish();
                        } else {
                            // Incorrect password
                            Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Email not found in Firestore
                        Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Error occurred while querying Firestore
                    Toast.makeText(LoginActivity.this, "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void signUpPage(){
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void forgotPassPage(){
        Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

//    private void checkRememberMe() {
//        String savedEmail = sharedPreferences.getString("email", null);
//        String savedPassword = sharedPreferences.getString("password", null);
//
//        if (savedEmail != null && savedPassword != null) {
//            emailInput.setText(savedEmail);
//            passwordInput.setText(savedPassword);
//            rememberMeCheckbox.setChecked(true);
//        }
//    }
//
//    private void saveRememberMe(String email, String password) {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("email", email);
//        editor.putString("password", password);
//        editor.apply();
//    }
//
//    private void clearRememberMe() {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.remove("email");
//        editor.remove("password");
//        editor.apply();
//    }
}