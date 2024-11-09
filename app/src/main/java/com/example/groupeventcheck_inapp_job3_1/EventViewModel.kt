package com.example.groupeventcheck_inapp_job3_1

import Event
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.job3nsda.Participant
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class EventViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // LiveData for a single event
    private val _event = MutableLiveData<Event?>()
    val event: LiveData<Event?> get() = _event

    // LiveData for participant list
    private val _participants = MutableLiveData<List<Participant>>()
    val participants: LiveData<List<Participant>> get() = _participants

    // Load event details by eventId
    fun loadEvent(eventId: String) {
        db.collection("events").document(eventId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val event = document.toObject<Event>()
                    _event.value = event
                    event?.let {
                        // Load participants using the event's participants list
                        loadParticipants(it.participants)
                    }
                } else {
                    _event.value = null // Event does not exist
                }
            }
            .addOnFailureListener { e ->
                // Handle failure
                e.printStackTrace()
                _event.value = null // Set event to null on failure
            }
    }

    // Load participants for a specific event using a list of user IDs
    private fun loadParticipants(participantIds: List<String>) {
        if (participantIds.isEmpty()) {
            _participants.value = emptyList() // No participants to load
            return
        }

        val participantList = mutableListOf<Participant>()
        val participantFetches = participantIds.map { userId ->
            db.collection("participants").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val participant = document.toObject<Participant>()
                        participant?.let { participantList.add(it) }
                    }
                }
                .addOnFailureListener { e ->
                    // Handle failure
                    e.printStackTrace()
                }
        }

        // Execute all fetch operations and notify when done
        Tasks.whenAllComplete(participantFetches).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _participants.value = participantList // Update LiveData with the loaded participants
            } else {
                // Handle the failure to fetch all participants
                _participants.value = participantList // Update with what was successfully loaded
            }
        }
    }

    // Add or update participant check-in details
    fun updateParticipant(eventId: String, participant: Participant) {
        db.collection("events").document(eventId)
            .collection("participants")
            .document(participant.userId)
            .set(participant)
            .addOnSuccessListener {
                // Successfully updated participant
                loadParticipants(_event.value?.participants ?: emptyList()) // Reload participants
            }
            .addOnFailureListener { e ->
                // Handle failure
                e.printStackTrace()
            }
    }
}
