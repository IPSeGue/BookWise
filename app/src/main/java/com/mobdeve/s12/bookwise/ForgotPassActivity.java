package com.mobdeve.s12.bookwise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import org.mindrot.jbcrypt.BCrypt;

public class ForgotPassActivity extends AppCompatActivity {

    private Button btnConfirm;
    private EditText emailInput, passWord, confirmPass;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        db = FirebaseFirestore.getInstance(); // Initialize Firestore
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
        String email = emailInput.getText().toString().trim();
        String newPassword = passWord.getText().toString().trim();
        String confirmPassword = confirmPass.getText().toString().trim();

        if (email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(ForgotPassActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(ForgotPassActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        } else {
            // Check if the user exists by email
            db.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String userId = document.getId();

                                // Hash the new password if required
                                String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

                                // Update the password in Firestore
                                db.collection("users").document(userId)
                                        .update("password", hashedPassword, "verifyPassword", newPassword)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(ForgotPassActivity.this, "Password reset successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ForgotPassActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(ForgotPassActivity.this, "Failed to reset password: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }
                        } else {
                            // User not found
                            Toast.makeText(ForgotPassActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ForgotPassActivity.this, "Error fetching user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
