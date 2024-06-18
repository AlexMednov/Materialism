package com.materialism

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.materialism.utils.DrawerUtils

class ViewFriendsActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var friendAdapter: FriendAdapter
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_view_friends)

    drawerLayout = findViewById(R.id.drawer_layout)
    navigationView = findViewById(R.id.nav_view)
    val menuIcon: ImageView = findViewById(R.id.menu_icon)
    val addFriendIcon: ImageView = findViewById(R.id.add_friend_icon)

    // Initialize the DrawerUtils to setup the drawer content
    DrawerUtils.setupDrawerContent(this, navigationView, drawerLayout)

    menuIcon.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }

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
        val dummyFriends = listOf(
            Friend("Name Surname", "Location: Emmen", "Items: 240"),
            // Add more dummy friends here
        )
        friendAdapter.submitList(dummyFriends)
    }

  private fun loadFriendsData(loggedInUserId: Int) {
    databaseAdapter.getFriendsUserIds(loggedInUserId) { friendUserIds ->
      databaseAdapter.getUsersInformation(friendUserIds) { users ->
        databaseAdapter.getItemsForUsers(friendUserIds) { items ->
          val friends =
              users.map { user ->
                Friend(user.name, "Location: ${user.location}", "Items: ${items}")
              }
          friendAdapter.submitList(friends)
        }
      }
    }
  }
}
