package com.mobdeve.s12.bookwise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogIn;
    private TextView btnSignUp, btnForgotPass, userName, passWord;
    private ImageButton facebook, goolge, x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogIn = findViewById(R.id.logIn);
        btnSignUp = findViewById(R.id.signUp);
        btnForgotPass = findViewById(R.id.forgotPass);
        userName = findViewById(R.id.userName);
        passWord = findViewById(R.id.passWord);
        facebook = findViewById(R.id.facebook);
        goolge = findViewById(R.id.google);
        x = findViewById(R.id.x);

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(userName, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userName.getText().toString().trim();
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
                        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
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