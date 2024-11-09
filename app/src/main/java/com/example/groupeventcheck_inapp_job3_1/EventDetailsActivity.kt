package com.example.groupeventcheck_inapp_job3_1

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EventDetailsActivity : AppCompatActivity() {

    private lateinit var eventNameTextView: TextView
    private lateinit var eventLocationTextView: TextView
    private lateinit var eventTimeTextView: TextView
    private lateinit var eventParticipantsTextView: TextView
    private lateinit var eventId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        // Initialize TextViews
        eventNameTextView = findViewById(R.id.textViewEventName)
        eventLocationTextView = findViewById(R.id.textViewEventLocation)
        eventTimeTextView = findViewById(R.id.textViewEventTime)
        eventParticipantsTextView = findViewById(R.id.textViewEventParticipants)

        // Get the event ID passed from the MainActivity
        eventId = intent.getStringExtra("EVENT_ID") ?: ""

        if (eventId.isNotEmpty()) {
            fetchEventDetails(eventId)
        } else {
            Toast.makeText(this, "Invalid event ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchEventDetails(eventId: String) {
        val db = Firebase.firestore

        db.collection("events").document(eventId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val eventName = document.getString("name") ?: "No Name"
                    val eventLocation = document.getString("location") ?: "No Location"
                    val eventTime = document.getString("time") ?: "No Time"
                    val participants = document.get("participants") as? List<String> ?: emptyList()

                    // Update TextViews with event details
                    eventNameTextView.text = eventName
                    eventLocationTextView.text = eventLocation
                    eventTimeTextView.text = eventTime
                    eventParticipantsTextView.text = if (participants.isNotEmpty()) {
                        participants.joinToString(", ")
                    } else {
                        "No Participants"
                    }
                } else {
                    Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error fetching event details: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}