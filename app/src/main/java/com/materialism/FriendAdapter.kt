package com.materialism

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Friend(val name: String, val location: String, val items: String)

class FriendAdapter(private val onClick: (Friend) -> Unit) :
    RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {

    private val friends = mutableListOf<Friend>()

    class FriendViewHolder(view: View, val onClick: (Friend) -> Unit) :
        RecyclerView.ViewHolder(view) {
        private val friendName: TextView = view.findViewById(R.id.friend_name)
        private val friendLocation: TextView = view.findViewById(R.id.friend_location)
        private val friendItems: TextView = view.findViewById(R.id.friend_items)
        private var currentFriend: Friend? = null

        init {
            view.setOnClickListener { currentFriend?.let { onClick(it) } }
        }

        fun bind(friend: Friend) {
            currentFriend = friend
            friendName.text = friend.name
            friendLocation.text = friend.location
            friendItems.text = friend.items
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_item, parent, false)
        return FriendViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(friends[position])
    }

    override fun getItemCount() = friends.size

    fun submitList(friendList: List<com.materialism.utils.Friend>) {
        friends.clear()
        friends.addAll(friendList)
        notifyDataSetChanged()
    }
}