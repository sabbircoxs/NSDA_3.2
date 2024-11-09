package com.example.groupeventcheck_inapp_job3_1

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.MapStyleOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val DEFAULT_LOCATION = LatLng(-34.0, 151.0) // Default location (Sydney) in case the user's location is unavailable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Enable the "My Location" layer on the map (disabled by default)
        enableMyLocation()

        // Set the map style (optional)
        setMapStyle()

        // Add a marker at the default location and move the camera
        mMap.addMarker(MarkerOptions().position(DEFAULT_LOCATION).title("Event Location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 10f))
    }

    private fun enableMyLocation() {
        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If permissions are not granted, request permissions
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        // Enable the user's location on the map (but default will be disabled)
        mMap.isMyLocationEnabled = true

        // Get the last known location of the user
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val userLocation = LatLng(it.latitude, it.longitude)
                mMap.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
            } ?: run {
                Toast.makeText(this, "Unable to determine your location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setMapStyle() {
        try {
            // Set the map style based on a JSON file (optional)
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Toast.makeText(this, "Style parsing failed.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Can't find style. Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle the result of the location permission request
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If permission is granted, enable location on the map
                enableMyLocation()
            } else {
                // If permission is denied, show a message or handle accordingly
                Toast.makeText(this, "Location permission is required to show your location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
