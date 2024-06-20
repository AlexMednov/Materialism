package com.materialism

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.materialism.database.firebaseDatabase.data.User
import com.materialism.utils.PasswordUtils
import com.materialism.utils.SessionManager

class LoginActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Check if user is already logged in
    if (SessionManager.isLoggedIn(this)) {
      navigateToMainPage()
    } else {
      setContentView(R.layout.login_activity)

      val loginButton = findViewById<Button>(R.id.login_button)
      val registerTextView = findViewById<TextView>(R.id.tv_register_here)

      loginButton.setOnClickListener {
        val email = findViewById<EditText>(R.id.username).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()

        if (validateInputs(email, password)) {
          loginUser(email, password)
        }
      }

      registerTextView.setOnClickListener {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
      }
    }
  }

  private fun validateInputs(email: String, password: String): Boolean {
    return when {
      email.isEmpty() -> {
        showToast("Please enter your email")
        false
      }
      password.isEmpty() -> {
        showToast("Please enter your password")
        false
      }
      else -> true
    }
  }

  private fun loginUser(email: String, password: String) {
    val databaseReference = FirebaseDatabase.getInstance().getReference("User")

    databaseReference
        .orderByChild("email")
        .equalTo(email)
        .addListenerForSingleValueEvent(
            object : ValueEventListener {
              override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                  for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                      if (PasswordUtils.verifyPassword(password, user.hashedPassword ?: "")) {
                        SessionManager.createSession(this@LoginActivity, user.id)
                        navigateToMainPage()
                      } else {
                        showToast("Invalid password")
                      }
                    }
                  }
                } else {
                  showToast("User not found")
                }
              }

              override fun onCancelled(databaseError: DatabaseError) {
                showToast("Login failed due to database error")
              }
            })
  }

  private fun navigateToMainPage() {
    val intent = Intent(this, MainPageActivity::class.java)
    startActivity(intent)
    finish()
  }

  private fun showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
  }
}
