package com.materialism

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.materialism.firebaseDatabase.data.Friend
import com.materialism.firebaseDatabase.data.User

class FriendAdapter(
    private val onClick: (Friend, User) -> Unit,
    private val getUserDetails: (Int) -> User?
) : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {

  private val friends = mutableListOf<Friend>()

  inner class FriendViewHolder(
      view: View,
      val onClick: (Friend, User) -> Unit,
      val getUserDetails: (Int) -> User?
  ) : RecyclerView.ViewHolder(view) {
    private val friendName: TextView = view.findViewById(R.id.friend_name)
    private val friendLocation: TextView = view.findViewById(R.id.friend_location)
    private val friendItems: TextView = view.findViewById(R.id.friend_items)
    private var currentFriend: Friend? = null

    init {
      view.setOnClickListener {
        currentFriend?.let {
          val userDetails = getUserDetails(it.userId2)
          if (userDetails != null) {
            onClick(it, userDetails)
            val context = view.context
            val intent =
                Intent(context, ViewFriendProfileActivity::class.java).apply {
                  putExtra("USER_ID", userDetails.id)
                  putExtra("USER_NAME", userDetails.name)
                  putExtra("USER_LOCATION", userDetails.location)
                  putExtra("USER_SCORE", userDetails.score)
                }
            context.startActivity(intent)
          }
        }
      }
    }

    //      fun bind(friend: Friend, user: User?) {
    //        currentFriend = friend
    //        user?.let {
    //          friendName.text = it.name
    //          friendLocation.text = it.location ?: "Unknown Location"
    //          friendItems.text = "Items: ${it.score}" // Example usage of score as items
    //        }
    //      }

    fun bind(friend: Friend) {
      currentFriend = friend
      val user = getUserDetails(friend.userId2)
      if (user != null) {
        friendName.text = user.name
        friendLocation.text = user.location ?: "Unknown Location"
        friendItems.text = "Items: ${user.score}" // Example usage of score as items
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_item, parent, false)
    return FriendViewHolder(view, onClick, getUserDetails)
  }

  override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
    holder.bind(friends[position])
  }

  //    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
  //      val friend = friends[position]
  //      val userDetails = getUserDetails(friend.userId2)
  //      holder.bind(friend, userDetails)
  //    }

  override fun getItemCount() = friends.size

  fun submitList(friendList: List<Friend>) {
    friends.clear()
    friends.addAll(friendList)
    notifyDataSetChanged()
  }
}
