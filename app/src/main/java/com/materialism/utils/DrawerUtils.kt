package com.materialism.utils

import android.app.Activity
import android.view.LayoutInflater
import android.widget.Button
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.materialism.R

object DrawerUtils {

  fun setupDrawerContent(
    activity: Activity,
    navigationView: NavigationView,
    drawerLayout: DrawerLayout
  ) {
    val headerView = LayoutInflater.from(activity).inflate(R.layout.burger_menu_layout, navigationView, false)
    navigationView.addHeaderView(headerView)

    headerView.findViewById<Button>(R.id.nav_profile).setOnClickListener {
      // Handle profile navigation
      drawerLayout.closeDrawers()
      // Add navigation logic here
    }
    headerView.findViewById<Button>(R.id.nav_settings).setOnClickListener {
      // Handle settings navigation
      drawerLayout.closeDrawers()
      // Add navigation logic here
    }
    headerView.findViewById<Button>(R.id.nav_support).setOnClickListener {
      // Handle support navigation
      drawerLayout.closeDrawers()
      // Add navigation logic here
    }
    headerView.findViewById<Button>(R.id.nav_logout).setOnClickListener {
      // Handle logout navigation
      drawerLayout.closeDrawers()
      // Add navigation logic here
    }
  }

  fun openDrawer(drawerLayout: DrawerLayout) {
    drawerLayout.openDrawer(GravityCompat.START)
  }
}