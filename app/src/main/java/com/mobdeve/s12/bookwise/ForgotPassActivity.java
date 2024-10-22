package com.mobdeve.s12.bookwise;

import android.content.Intent;
import android.os.Bundle;
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

public class ForgotPassActivity extends AppCompatActivity {

    private Button btnConfirm;
    private EditText emailInput, passWord, confirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        initViews();
        btnConfirm.setOnClickListener(v -> changePassword());
    }

    public void initViews(){
        emailInput = findViewById(R.id.fp_email);
        passWord = findViewById(R.id.fp_password);
        confirmPass = findViewById(R.id.fp_confirmPass);
        btnConfirm = findViewById(R.id.fp_confirm);
    }

    public void changePassword(){
        String email = emailInput.getText().toString();
        String newPassword = passWord.getText().toString();
        String confirmPassword = confirmPass.getText().toString();

        if (email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(ForgotPassActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(ForgotPassActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ForgotPassActivity.this, "Password reset successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ForgotPassActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}