package com.materialism

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.materialism.utils.DrawerUtils

class ViewFriendProfileActivity : AppCompatActivity() {

  private lateinit var drawerLayout: DrawerLayout
  private lateinit var navigationView: NavigationView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_view_friends_profile)

    val menuIcon: ImageButton = findViewById(R.id.menu_button)
    DrawerUtils.setupPopupMenu(this, menuIcon)
  }
}
