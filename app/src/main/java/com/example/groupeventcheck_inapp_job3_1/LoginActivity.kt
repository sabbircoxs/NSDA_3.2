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

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Bind UI elements
        val emailField: EditText = findViewById(R.id.editTextEmail)
        val passwordField: EditText = findViewById(R.id.editTextPassword)
        val loginButton: Button = findViewById(R.id.btnLogin)
        val registerText: TextView = findViewById(R.id.textViewRegister)

        // Login button click listener
        loginButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                signIn(email, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Register text click listener
        registerText.setOnClickListener {
            // Navigate to RegisterActivity
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, navigate to MainActivity
                    val intent = Intent(this, MainActivity::class.java) // Change to your main activity
                    startActivity(intent)
                    finish() // Close LoginActivity
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}