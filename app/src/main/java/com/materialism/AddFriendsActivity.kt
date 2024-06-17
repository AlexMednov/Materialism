package com.materialism

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.materialism.utils.DrawerUtils

class AddFriendsActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_friends)
    
    val menuIcon: ImageButton = findViewById(R.id.menu_icon)
    DrawerUtils.setupPopupMenu(this, menuIcon)
  }
}
