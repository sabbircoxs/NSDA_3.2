package com.example.groupeventcheck_inapp_job3_1

import android.Manifest  // Make sure to import this
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore

class CheckInActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_in)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val checkInButton: Button = findViewById(R.id.checkInButton)

        checkInButton.setOnClickListener {
            checkInUser()
        }
    }

    private fun checkInUser() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val userName = "User Name" // Replace with actual user name
                    val checkInTime = System.currentTimeMillis()

                    // Update Firestore
                    val checkInData = hashMapOf(
                        "userName" to userName,
                        "latitude" to location.latitude,
                        "longitude" to location.longitude,
                        "checkInTime" to checkInTime
                    )

                    db.collection("checkIns").add(checkInData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Check-in successful!", Toast.LENGTH_SHORT).show()
                            // Optionally, you can redirect to the map activity to show the location
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }
}
