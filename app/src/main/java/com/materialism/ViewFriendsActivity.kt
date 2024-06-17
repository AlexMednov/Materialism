package com.materialism

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.materialism.utils.DrawerUtils

class ViewFriendsActivity : AppCompatActivity() {

  private lateinit var drawerLayout: DrawerLayout
  private lateinit var navigationView: NavigationView
  private lateinit var recyclerView: RecyclerView
  private lateinit var friendAdapter: FriendAdapter

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

    // Set up RecyclerView for friend list
    recyclerView = findViewById(R.id.recycler_view)
    recyclerView.layoutManager = LinearLayoutManager(this)
    friendAdapter = FriendAdapter { friend ->
      val intent = Intent(this, ViewFriendProfileActivity::class.java)
      startActivity(intent)
    }
    recyclerView.adapter = friendAdapter

    // Load dummy data
    loadDummyData()
  }

  private fun loadDummyData() {
    val dummyFriends =
        listOf(
            Friend("Name Surname", "Location: Emmen", "Items: 240"),
            // Add more dummy friends here
        )
    friendAdapter.submitList(dummyFriends)
  }
}
