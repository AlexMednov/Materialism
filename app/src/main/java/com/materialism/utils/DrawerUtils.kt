package com.materialism.utils

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.materialism.R

object DrawerUtils {
  private const val TAG = "DrawerUtils"

  fun setupDrawerContent(
    activity: Activity,
    navigationView: NavigationView,
    drawerLayout: DrawerLayout
  ) {
    val headerView =
      LayoutInflater.from(activity).inflate(R.layout.burger_menu_layout, navigationView, false)
    navigationView.addHeaderView(headerView)
    Log.d(TAG, "Header view added")

    headerView.findViewById<Button>(R.id.nav_profile).setOnClickListener {
      Log.d(TAG, "Profile button clicked")
      drawerLayout.closeDrawers()
      // Add navigation logic here
    }
    headerView.findViewById<Button>(R.id.nav_settings).setOnClickListener {
      Log.d(TAG, "Settings button clicked")
      drawerLayout.closeDrawers()
      // Add navigation logic here
    }
    val supportButton = headerView.findViewById<Button>(R.id.nav_support)
    Log.d(TAG, "Support button found: $supportButton")

    supportButton.setOnClickListener {
      Log.d(TAG, "Support button clicked")
      println("Support button clicked")
      drawerLayout.closeDrawers()
      // Add navigation logic here
    }
    headerView.findViewById<Button>(R.id.nav_logout).setOnClickListener {
      Log.d(TAG, "Logout button clicked")
      drawerLayout.closeDrawers()
      // Add navigation logic here
    }
  }

  fun openDrawer(drawerLayout: DrawerLayout) {
    drawerLayout.openDrawer(GravityCompat.START)
    Log.d(TAG, "Drawer opened")
  }
}
