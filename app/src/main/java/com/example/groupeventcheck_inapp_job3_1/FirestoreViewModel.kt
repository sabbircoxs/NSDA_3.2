package com.example.groupeventcheck_inapp_job3_1

import Event
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreViewModel : ViewModel() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> get() = _events

    fun fetchEvents() {
        db.collection("events")
            .get()
            .addOnSuccessListener { documents ->
                val eventList = documents.map { document ->
                    document.toObject(Event::class.java)
                }
                _events.value = eventList
            }
            .addOnFailureListener { exception ->
                // Handle the error
            }
    }

    fun createEvent(event: Event) {
        db.collection("events").add(event)
            .addOnSuccessListener {
                // Event created successfully
                fetchEvents()  // Refresh the event list
            }
            .addOnFailureListener { exception ->
                // Handle the error
            }
    }
}
