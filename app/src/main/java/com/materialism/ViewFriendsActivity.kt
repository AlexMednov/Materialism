package com.materialism

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.materialism.utils.DrawerUtils

class ViewFriendsActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_view_friends)

    val menuIcon: ImageButton = findViewById(R.id.menu_icon)
    val addFriendIcon: ImageButton = findViewById(R.id.add_friend_icon)

    DrawerUtils.setupPopupMenu(this, menuIcon)

    addFriendIcon.setOnClickListener {
      val intent = Intent(this, AddFriendsActivity::class.java)
      startActivity(intent)
    }
  }
}
