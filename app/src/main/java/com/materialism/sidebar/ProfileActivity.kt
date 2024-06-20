package com.materialism.sidebar

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.materialism.R
import com.materialism.adapter.DatabaseAdapter
import com.materialism.database.localDatabase.DatabaseManager
import com.materialism.utils.DrawerUtils
import com.materialism.utils.SessionManager

class ProfilePageActivity : AppCompatActivity() {
  private lateinit var databaseAdapter: DatabaseAdapter
  private lateinit var usernameTextView: TextView
  private lateinit var emailTextView: TextView
  private lateinit var locationTextView: TextView
  private lateinit var menuIcon: ImageButton

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_profile_page)

    menuIcon = findViewById(R.id.ic_menu)
    usernameTextView = findViewById(R.id.profile_username)
    emailTextView = findViewById(R.id.profile_email)
    locationTextView = findViewById(R.id.tv_location)

    DrawerUtils.setupPopupMenu(this, menuIcon)

    databaseAdapter = DatabaseAdapter(DatabaseManager(this))

    val userId = SessionManager.getUserId(this)
    if (userId != -1) { // Check if userId is valid
      fetchAndDisplayUserInfo(userId)
    }
  }

  private fun fetchAndDisplayUserInfo(userId: Int) {
    databaseAdapter.getSingleUserInformation(userId) { user ->
      user?.let {
        usernameTextView.text = it.name
        emailTextView.text = it.email
        locationTextView.text = it.location
      }
          ?: run {
            // Handle case where user is not found
            usernameTextView.text = "User not found"
            emailTextView.text = ""
          }
    }
  }
}
