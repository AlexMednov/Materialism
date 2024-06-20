package com.materialism

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.materialism.firebaseDatabase.data.User
import com.materialism.utils.DrawerUtils
import com.materialism.utils.SessionManager
import com.materialism.DatabaseAdapter.*
import com.google.firebase.database.FirebaseDatabase

class ProfilePageActivity : AppCompatActivity() {
    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var menuIcon: ImageButton
    private lateinit var applyChangesButton: Button
    private lateinit var databaseAdapter: DatabaseAdapter

    private var loggedInUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        menuIcon = findViewById(R.id.ic_menu)
        usernameTextView = findViewById(R.id.profile_username)
        emailTextView = findViewById(R.id.profile_email)
        locationTextView = findViewById(R.id.tv_location)
        nameEditText = findViewById(R.id.et_name)
        emailEditText = findViewById(R.id.et_email)
        locationEditText = findViewById(R.id.et_location)
        applyChangesButton = findViewById(R.id.btn_apply_changes)
        DrawerUtils.setupPopupMenu(this, menuIcon)

        databaseAdapter = DatabaseAdapter(DatabaseManager(this))

        val userId = SessionManager.getUserId(this)
        Log.d("ProfilePageActivity", "User ID from session: $userId")
        if (userId != -1) { // Check if userId is valid
            fetchAndDisplayUserInfo(userId)
        }

        applyChangesButton.setOnClickListener {
            updateUserInfo()
        }
    }

    private fun fetchAndDisplayUserInfo(userId: Int) {
        Log.d("ProfilePageActivity", "Fetching user info for ID: $userId")

        databaseAdapter.getSingleUserInformation(userId) { user ->
            user?.let {
                loggedInUser = it
                Log.d("ProfilePageActivity", "User fetched: $it")
                usernameTextView.text = it.name
                emailTextView.text = it.email
                locationTextView.text = it.location
                nameEditText.setText(it.name)
                emailEditText.setText(it.email)
                locationEditText.setText(it.location)

            } ?: run {
                Log.d("ProfilePageActivity", "User not found for ID: $userId")
                // Handle case where user is not found

                // Handle case where user is not found
                usernameTextView.text = "User not found"
                emailTextView.text = ""
                locationTextView.text = ""
            }
        }
    }

    private fun updateUserInfo() {
        val user = loggedInUser ?: run {
            Log.e("ProfilePageActivity", "Logged in user is null")
            Toast.makeText(this, "Error: Logged in user is null", Toast.LENGTH_SHORT).show()
            return
        }

            val newName = nameEditText.text.toString()
            val newEmail = emailEditText.text.toString()
            val newLocation = locationEditText.text.toString()

        val userUpdated = user.copy(
            name = newName,
            email = newEmail,
            location = newLocation
        )

        Log.d("ProfilePageActivity", "Updating user information: $userUpdated")

        databaseAdapter.updateUserInformation(user) { success ->
                if (success) {
                    Log.d("ProfilePageActivity", "User information updated successfully")

                    if (newEmail != user.email) { // If email has changed, log out the user.
                        Log.d("ProfilePageActivity", "Email changed. Logging out the user.")

                        SessionManager.logout(this)
                        // Optionally, redirect to login screen or finish the activity
                        finish()
                    } else {
                        fetchAndDisplayUserInfo(user.id)
                    }
                } else {
                    Log.e("ProfilePageActivity", "Failed to update user information")
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()

                    // Handle update failure (e.g., show a toast or a message)
                    // Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
