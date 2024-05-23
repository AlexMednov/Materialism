package com.materialism

import android.app.Activity
import android.widget.Button
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

object DrawerUtils {

    fun setupDrawerContent(activity: Activity, navigationView: NavigationView, drawerLayout: DrawerLayout) {
        navigationView.findViewById<Button>(R.id.nav_profile).setOnClickListener {
            // Handle profile navigation
            drawerLayout.closeDrawers()
            // Add navigation logic here
        }
        navigationView.findViewById<Button>(R.id.nav_settings).setOnClickListener {
            // Handle settings navigation
            drawerLayout.closeDrawers()
            // Add navigation logic here
        }
        navigationView.findViewById<Button>(R.id.nav_support).setOnClickListener {
            // Handle support navigation
            drawerLayout.closeDrawers()
            // Add navigation logic here
        }
        navigationView.findViewById<Button>(R.id.nav_logout).setOnClickListener {
            // Handle logout navigation
            drawerLayout.closeDrawers()
            // Add navigation logic here
        }
    }

    fun openDrawer(drawerLayout: DrawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START)
    }
}
