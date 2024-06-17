package com.materialism

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.materialism.utils.DrawerUtils

class AddFriendsActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        val menuIcon: ImageView = findViewById(R.id.menu_icon)

        // Initialize the DrawerUtils to setup the drawer content
        DrawerUtils.setupDrawerContent(this, navigationView, drawerLayout)


        menuIcon.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
    }
}
