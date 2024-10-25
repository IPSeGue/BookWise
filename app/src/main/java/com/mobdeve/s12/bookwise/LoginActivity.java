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

import java.util.ArrayList;
import java.util.List;

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

    public void logIn(){
        String email = emailInput.getText().toString().trim();
        String password = passWord.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
        } else {
            // Replace with your actual credential check logic
            if (email.equals("123") && password.equals("pw")) {
                Toast.makeText(LoginActivity.this, "Sign-In Successful", Toast.LENGTH_SHORT).show();

//                        if (rememberMeCheckbox.isChecked()) {
//                            saveRememberMe(email, password);
//                        } else {
//                            clearRememberMe();
//                        }

                // Change this line to redirect to StepCounterActivity
                DataGeneratorBooks dataGenerator = new DataGeneratorBooks();
                dataGenerator.fetchAndSaveBooks("fiction", new GoogleBookAPI.OnBooksFetchedListener() {
                    @Override
                    public void onBooksFetched(List<Bookitem> books) {
                        DataGeneratorBooks.getInstance().setBookList(books);

                        // Pass the books to HomeActivity
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);

                        finish();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        }
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