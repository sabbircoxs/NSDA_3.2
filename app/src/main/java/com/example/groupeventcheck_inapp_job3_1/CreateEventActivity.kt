package com.example.groupeventcheck_inapp_job3_1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateEventActivity : AppCompatActivity() {

    private lateinit var eventNameField: EditText
    private lateinit var eventLocationField: EditText
    private lateinit var eventTimeField: EditText
    private lateinit var createEventButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore  // Declare the db variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance() // Initialize db here

        eventNameField = findViewById(R.id.editTextEventName)
        eventLocationField = findViewById(R.id.editTextEventLocation)
        eventTimeField = findViewById(R.id.editTextEventTime)
        createEventButton = findViewById(R.id.btnCreateEvent)

        createEventButton.setOnClickListener {
            val name = eventNameField.text.toString().trim()
            val location = eventLocationField.text.toString().trim()
            val time = eventTimeField.text.toString().trim()

            if (name.isNotEmpty() && location.isNotEmpty() && time.isNotEmpty()) {
                createEvent(name, location, time)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createEvent(name: String, location: String, time: String) {
        // Create a unique event ID
        val eventId = db.collection("events").document().id

        val event = hashMapOf(
            "eventId" to eventId,
            "name" to name,
            "location" to location,
            "time" to time,
            "participants" to listOf<String>() // Empty list of participants initially
        )

        db.collection("events")
            .document(eventId) // Use eventId for the document reference
            .set(event)
            .addOnSuccessListener {
                Toast.makeText(this, "Event created successfully!", Toast.LENGTH_SHORT).show()
                // Optionally navigate back to main activity or another screen
                finish() // Close the current activity
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to create event: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
