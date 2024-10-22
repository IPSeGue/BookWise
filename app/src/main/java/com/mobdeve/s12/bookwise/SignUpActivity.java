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

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private Button btnRegister;
    private TextView  btnLogin;
    private EditText fullName, emailInput, passWord, confirmPass;
    private ImageButton facebook, goolge, x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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

    public void register(){
        String re_fullName = fullName.getText().toString().trim();
        String re_email = emailInput.getText().toString().trim();
        String re_password = passWord.getText().toString().trim();
        String re_confirmPassword = confirmPass.getText().toString().trim();

        if (isInputValid(re_fullName, re_email, re_password, re_confirmPassword)) {
            Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isInputValid(String re_fullName, String re_email, String re_password, String re_confirmPassword){
        List<String> errorMessages = new ArrayList<>();
        boolean isValid = true;

        if (re_fullName.isEmpty()) {
            errorMessages.add("Full Name cannot be empty");
            isValid = false;
        }

        if (re_email.isEmpty() || !isEmailValid(re_email)) {
            errorMessages.add("Use your DLSU email.");
            isValid = false;
        }

        if (re_password.isEmpty() || re_password.length() < 6) {
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

    private boolean isEmailValid(String email) {
        return email.endsWith("@dlsu.edu.ph");
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