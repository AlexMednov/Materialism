package com.materialism.friend

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.materialism.R
import com.materialism.database.localDatabase.DatabaseManager
import com.materialism.utils.DrawerUtils

class AddFriendsActivity : AppCompatActivity() {

  private var databaseManager = DatabaseManager(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_friends)
    databaseManager.open()

    val menuIcon: ImageButton = findViewById(R.id.ic_menu)
    DrawerUtils.setupPopupMenu(this, menuIcon)

    val backButton: ImageButton = findViewById(R.id.back_button)
    backButton.setOnClickListener { onBackPressed() }
  }
}
