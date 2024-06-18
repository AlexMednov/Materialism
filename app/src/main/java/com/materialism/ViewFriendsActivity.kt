package com.materialism

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.materialism.utils.DrawerUtils
import com.materialism.utils.Friend

class ViewFriendsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var friendAdapter: FriendAdapter
    private lateinit var databaseAdapter: DatabaseAdapter // Declare it as a lateinit var

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_friends)

        val databaseManager = DatabaseManager(this) // Assuming DatabaseManager requires a context
        databaseAdapter = DatabaseAdapter(databaseManager) // Pass the databaseManager instance

        val addFriendIcon: ImageView = findViewById(R.id.add_friend_icon)
        val menuIcon: ImageButton = findViewById(R.id.ic_menu)
        DrawerUtils.setupPopupMenu(this, menuIcon)

        addFriendIcon.setOnClickListener {
            val intent = Intent(this, AddFriendsActivity::class.java)
            startActivity(intent)
        }

        // Set up RecyclerView for friend list
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        friendAdapter = FriendAdapter { _ -> // Rename unused parameter to _
            val intent = Intent(this, ViewFriendProfileActivity::class.java)
            startActivity(intent)
        }
        recyclerView.adapter = friendAdapter

        // Load dummy data
        loadDummyData()

        // Example usage of loadFriendsData
        val loggedInUserId = getLoggedInUserId() // Replace with dynamic user ID retrieval
        loadFriendsData(loggedInUserId)
    }
    recyclerView.adapter = friendAdapter

    // Load dummy data
    loadDummyData()

    // Example usage of loadFriendsData
    val loggedInUserId = getLoggedInUserId() // Replace with dynamic user ID retrieval
    loadFriendsData(loggedInUserId)
  }

  private fun loadDummyData() {
    val dummyFriends =
        listOf(
            Friend("Name Surname", "Location: Emmen", "Items: 240")
            // Add more dummy friends here
            )
    friendAdapter.submitList(dummyFriends)
  }

  private fun loadFriendsData(loggedInUserId: Int) {
    databaseAdapter.getFriendsUserIds(loggedInUserId) { friendUserIds ->
      databaseAdapter.getUsersInformation(friendUserIds) { users ->
        databaseAdapter.getItemsForUsers(friendUserIds) { items ->
          val friends =
              users.map { user -> Friend(user.name, "Location: ${user.location}", "Items: $items") }
          friendAdapter.submitList(friends)
        }
      }
    }
  }

  private fun getLoggedInUserId(): Int {
    // Replace with actual logic to get the logged-in user ID
    return 123
  }
}
