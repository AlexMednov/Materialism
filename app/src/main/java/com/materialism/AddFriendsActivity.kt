package com.materialism

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.navigation.NavigationView
import com.materialism.utils.DrawerUtils

class AddFriendsActivity : AppCompatActivity() {

    private var databaseManager = DatabaseManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)
        databaseManager.open()

        val menuIcon: ImageButton = findViewById(R.id.ic_menu)
        DrawerUtils.setupPopupMenu(this, menuIcon)

    }
}
