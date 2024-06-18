package com.materialism

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.materialism.firebaseDatabase.data.User
import com.materialism.utils.PasswordUtils

class RegisterActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var etLocation: EditText
    private lateinit var btnConfirm: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etEmail = findViewById(R.id.et_email)
        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        etConfirmPassword = findViewById(R.id.et_confirm_password)
        etLocation = findViewById(R.id.et_location)
        btnConfirm = findViewById(R.id.btn_confirm)

        btnConfirm.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()
            val location = etLocation.text.toString().trim()

            if (validateInputs(email, username, password, confirmPassword)) {
                checkIfUserExists(email, username, password, location)
            }
        }
    }

    private fun validateInputs(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Please enter a valid email"
            return false
        }
        if (TextUtils.isEmpty(username)) {
            etUsername.error = "Please enter a username"
            return false
        }
        if (TextUtils.isEmpty(password) || password.length < 6) {
            etPassword.error = "Password must be at least 6 characters"
            return false
        }
        if (password != confirmPassword) {
            etConfirmPassword.error = "Passwords do not match"
            return false
        }
        return true
    }

    private fun checkIfUserExists(
        email: String,
        username: String,
        password: String,
        location: String
    ) {
        val database = FirebaseDatabase.getInstance().reference.child("User")

        // Check by email
        database
            .orderByChild("email")
            .equalTo(email)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Email already registered",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            // Check by username
                            database
                                .orderByChild("name")
                                .equalTo(username)
                                .addListenerForSingleValueEvent(
                                    object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.exists()) {
                                                Toast.makeText(
                                                    this@RegisterActivity,
                                                    "Username already taken",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()
                                            } else {
                                                createUser(email, username, password, location)
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Toast.makeText(
                                                this@RegisterActivity,
                                                "Database error: ${error.message}",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    })
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Database error: ${error.message}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                })
    }

    private fun createUser(email: String, username: String, password: String, location: String) {
        val database = FirebaseDatabase.getInstance().reference
        val userId = database.child("User").push().key ?: return

        // Generate a salt and hash the password
        val salt = PasswordUtils.generateSalt()
        val hashedPassword = PasswordUtils.hashPassword(password, salt)

        val user =
            User(
                id = userId.hashCode(),
                name = username,
                email = email,
                hashedPassword = hashedPassword,
                isRegistered = true,
                location = location
            )

        database.child("User").child(userId).setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                // Redirect to login or another activity if necessary
            } else {
                Toast.makeText(this, "User registration failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
