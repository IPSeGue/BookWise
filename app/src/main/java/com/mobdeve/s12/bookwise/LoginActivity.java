package com.mobdeve.s12.bookwise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.CallbackManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {

    private static final int GOOGLE_SIGN_IN_CODE = 101;
    private static final String TAG = "LoginActivity";

    private Button btnLogIn;
    private TextView btnSignUp, btnForgotPass;
    private EditText emailInput, passWord;
    private ImageButton facebook, goolge, x;

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        callbackManager = CallbackManager.Factory.create();

        // Initialize Google Sign-In
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("816873376665-7qubl8s3p03j0uml5vbesdsvpkps8ugp.apps.googleusercontent.com") // Your client ID here
                .requestEmail()
                .build();

        fetchBooksAndProceed();

        btnLogIn.setOnClickListener(v -> logIn());
        btnSignUp.setOnClickListener(v -> signUpPage());
        btnForgotPass.setOnClickListener(v -> forgotPassPage());

        // Handle Facebook Login
        facebook.setOnClickListener(v -> {
            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    Toast.makeText(LoginActivity.this, "Facebook Login Cancelled", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(LoginActivity.this, "Facebook Login Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Handle Google Login
        goolge.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN_CODE);
        });

        // Handle Twitter Login
        x.setOnClickListener(v -> {
            // Initialize Twitter login logic here
            // Refer to the Twitter SDK documentation if needed
            Toast.makeText(LoginActivity.this, "Twitter Login Coming Soon!", Toast.LENGTH_SHORT).show();
        });

        emailInput.setOnClickListener(v -> {
            emailInput.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(emailInput, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    public void initViews() {
        btnLogIn = findViewById(R.id.l_logIn);
        btnSignUp = findViewById(R.id.l_signUp);
        btnForgotPass = findViewById(R.id.l_forgotPass);
        emailInput = findViewById(R.id.l_email_Input);
        passWord = findViewById(R.id.l_passWord);
        facebook = findViewById(R.id.l_facebook);
        goolge = findViewById(R.id.l_google);
        x = findViewById(R.id.l_x);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Toast.makeText(LoginActivity.this, "Facebook Login Successful", Toast.LENGTH_SHORT).show();
                        navigateToHome();
                    } else {
                        Toast.makeText(LoginActivity.this, "Facebook Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle Facebook callback
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Handle Google Sign-In result
        if (requestCode == GOOGLE_SIGN_IN_CODE) {
            try {
                GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);
                if (account != null) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    firebaseAuth.signInWithCredential(credential)
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Google Login Successful", Toast.LENGTH_SHORT).show();
                                    navigateToHome();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Google Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            } catch (ApiException e) {
                Log.e(TAG, "Google Sign-In Error: " + e.getMessage(), e);
                Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    // Existing methods remain unchanged (fetchBooksAndProceed, logIn, signUpPage, forgotPassPage, etc.)
}