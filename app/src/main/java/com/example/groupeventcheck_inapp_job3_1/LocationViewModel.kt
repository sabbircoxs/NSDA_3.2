package com.example.groupeventcheck_inapp_job3_1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import android.content.Context
import android.location.Location

class LocationViewModel(context: Context) : ViewModel() {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val _currentLocation = MutableLiveData<Location>()
    val currentLocation: LiveData<Location> get() = _currentLocation

    fun getCurrentLocation() {
        fusedLocationClient.lastLocation.addOnCompleteListener { task: Task<Location> ->
            if (task.isSuccessful && task.result != null) {
                _currentLocation.value = task.result
            } else {
                // Handle the error
            }
        }
    }
}
