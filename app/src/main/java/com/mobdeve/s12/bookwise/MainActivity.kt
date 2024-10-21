package com.mobdeve.s12.bookwise

import android.media.Image
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {

    private lateinit var btnLogIn: Button
    private lateinit var btnSignUp: TextView
    private lateinit var btnForgotPass: TextView
    private lateinit var userName: EditText
    private lateinit var passWord: EditText
    private lateinit var facebook: ImageButton
    private lateinit var google: ImageButton
    private lateinit var x: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login);
        initializeUI()

    }
    private fun initializeUI(){
        btnLogIn = findViewById(R.id.logIn)
        btnSignUp = findViewById(R.id.signUp)
        btnForgotPass = findViewById(R.id.forgotPass)
        userName = findViewById(R.id.userName)
        passWord = findViewById(R.id.passWord)
        facebook = findViewById(R.id.facebook)
        google = findViewById(R.id.google)
        x = findViewById(R.id.x)
    }

}