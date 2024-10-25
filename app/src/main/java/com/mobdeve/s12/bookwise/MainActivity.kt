package com.mobdeve.s12.bookwise

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnStart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart = findViewById(R.id.Start)

        // Set up onClickListeners using lambda expressions
        btnStart.setOnClickListener { onSignIn() }

    }

    private fun onSignIn() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
