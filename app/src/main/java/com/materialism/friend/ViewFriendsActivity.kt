package com.materialism.friend

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.materialism.R
import com.materialism.adapter.DatabaseAdapter
import com.materialism.adapter.FriendAdapter
import com.materialism.database.firebaseDatabase.data.Friend
import com.materialism.database.firebaseDatabase.data.User
import com.materialism.database.localDatabase.DatabaseManager
import com.materialism.utils.DrawerUtils

class ViewFriendsActivity : AppCompatActivity() {

  private lateinit var recyclerView: RecyclerView
  private lateinit var friendAdapter: FriendAdapter
  private lateinit var databaseAdapter: DatabaseAdapter
  private val userDetailsMap = mutableMapOf<Int, User>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_view_friends)

    val databaseManager = DatabaseManager(this)
    databaseAdapter = DatabaseAdapter(databaseManager)

    val addFriendIcon: ImageButton = findViewById(R.id.add_friend_icon)
    val menuIcon: ImageButton = findViewById(R.id.ic_menu)
    DrawerUtils.setupPopupMenu(this, menuIcon)

    addFriendIcon.setOnClickListener {
      val intent = Intent(this, AddFriendsActivity::class.java)
      startActivity(intent)
    }

    recyclerView = findViewById(R.id.recycler_view)
    recyclerView.layoutManager = LinearLayoutManager(this)
    friendAdapter =
        FriendAdapter(
            { friend, user ->
              val intent = Intent(this, ViewFriendProfileActivity::class.java)
              startActivity(intent)
            },
            { userId -> userDetailsMap[userId] })
    recyclerView.adapter = friendAdapter

    loadFriendsData(getLoggedInUserId())
  }

  private fun loadDummyData() {
    val dummyUsers =
        listOf(
            User(id = 1, name = "John Doe", location = "New York", score = 120),
            User(id = 2, name = "Jane Smith", location = "London", score = 200),
            User(id = 3, name = "Sam Brown", location = "Sydney", score = 300),
            User(id = 4, name = "Lisa White", location = "Toronto", score = 400))
    dummyUsers.forEach { user -> userDetailsMap[user.id] = user }

    val dummyFriends =
        listOf(
            Friend(userId1 = 0, userId2 = 1),
            Friend(userId1 = 0, userId2 = 2),
            Friend(userId1 = 0, userId2 = 3),
            Friend(userId1 = 0, userId2 = 4))
    friendAdapter.submitList(dummyFriends)
  }

  private fun loadFriendsData(loggedInUserId: Int) {
    databaseAdapter.getFriendsUserIds(loggedInUserId) { friendUserIds ->
      databaseAdapter.getUsersInformation(friendUserIds) { users ->
        // Clear existing entries, to avoid stale data
        userDetailsMap.clear()
        // Fill userDetailsMap to have user information
        users.forEach { user -> userDetailsMap[user.id] = user }
        // Create Friend list based on the user information
        val friends = friendUserIds.map { friendUserId -> Friend(loggedInUserId, friendUserId) }

        // Update adapter with new friends list
        friendAdapter.submitList(friends)
      }
    }
  }

  private fun getLoggedInUserId(): Int {
    // Replace with actual logic to get the logged-in user ID
    return 1
  }
}
