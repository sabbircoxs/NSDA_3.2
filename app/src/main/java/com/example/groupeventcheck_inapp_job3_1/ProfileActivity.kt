package com.example.groupeventcheck_inapp_job3_1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileNameTextView: TextView
    private lateinit var profileEmailTextView: TextView
    private lateinit var editNameButton: Button
    private lateinit var editEmailButton: Button
    private lateinit var saveChangesButton: Button

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize Views
        profileNameTextView = findViewById(R.id.profileNameTextView)
        profileEmailTextView = findViewById(R.id.profileEmailTextView)
        editNameButton = findViewById(R.id.editNameButton)
        editEmailButton = findViewById(R.id.editEmailButton)
        saveChangesButton = findViewById(R.id.saveChangesButton)

        // Observe profile data
        profileViewModel.profileData.observe(this, Observer { profile ->
            profileNameTextView.text = profile.name
            profileEmailTextView.text = profile.email
        })

        // Click listeners for buttons
        editNameButton.setOnClickListener {
            // Logic to edit name (e.g., show a dialog or a new activity)
        }

        editEmailButton.setOnClickListener {
            // Logic to edit email (e.g., show a dialog or a new activity)
        }

        saveChangesButton.setOnClickListener {
            // Logic to save changes
            // E.g., update the ViewModel with new values
        }
    }
}