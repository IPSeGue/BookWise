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
import com.google.firebase.auth.FirebaseAuth;
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
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login successful, get the UID of the logged-in user
                        String userId = auth.getCurrentUser().getUid();

                        // Optional: Get user data from Firestore, if needed
                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                        firestore.collection("users").document(userId).get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        // User data exists, proceed with the next activity
                                        Toast.makeText(LoginActivity.this, "Sign-In Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Handle case where user document doesn't exist
                                        Toast.makeText(LoginActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(LoginActivity.this, "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        // Login failed
                        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LoginActivity.this, "Authentication error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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