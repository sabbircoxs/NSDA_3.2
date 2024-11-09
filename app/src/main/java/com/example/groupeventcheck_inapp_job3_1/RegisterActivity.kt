package com.example.groupeventcheck_inapp_job3_1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Optional: Enables edge-to-edge layout
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Bind UI elements
        val emailField: EditText = findViewById(R.id.editTextEmail)
        val passwordField: EditText = findViewById(R.id.editTextPassword)
        val confirmPasswordField: EditText = findViewById(R.id.editTextConfirmPassword)
        val registerButton: Button = findViewById(R.id.btnRegister)
        val loginText: TextView = findViewById(R.id.textViewLogin)

        // Register button click listener
        registerButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val confirmPassword = confirmPasswordField.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword) {
                registerUser(email, password)
            } else {
                Toast.makeText(this, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }

        // Login text click listener
        loginText.setOnClickListener {
            // Navigate to LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registration success, navigate to login
                    Toast.makeText(baseContext, "Registration successful.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish() // Close RegisterActivity
                } else {
                    // If registration fails, display a message to the user.
                    Toast.makeText(baseContext, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}