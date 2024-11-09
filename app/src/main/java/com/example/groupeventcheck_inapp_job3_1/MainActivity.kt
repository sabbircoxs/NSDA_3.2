package com.example.groupeventcheck_inapp_job3_1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var viewEventsButton: Button
    private lateinit var createEventButton: Button
    private lateinit var profileButton: Button
    private lateinit var mapsButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Log current user
        Log.d("MainActivity", "Current User: ${auth.currentUser}")

        // Check if the user is signed in
        if (auth.currentUser == null) {
            // User is not signed in, navigate to Login Activity
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Close MainActivity
            return // Prevent further execution
        }

        setContentView(R.layout.activity_main)

        // Initialize buttons
        viewEventsButton = findViewById(R.id.viewEventsButton)
        createEventButton = findViewById(R.id.createEventButton)
        profileButton = findViewById(R.id.profileButton)
        mapsButton = findViewById(R.id.mapsButton)

        // Set up button click listeners
        viewEventsButton.setOnClickListener {
            val intent = Intent(this, EventDetailsActivity::class.java)
            startActivity(intent)
        }

        createEventButton.setOnClickListener {
            val intent = Intent(this, CreateEventActivity::class.java)
            startActivity(intent)
        }

        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        mapsButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }
}