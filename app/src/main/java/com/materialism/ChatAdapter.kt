package com.materialism

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val messages: List<String>) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    private val MESSAGE_TYPE_SENT = 1
    private val MESSAGE_TYPE_RECEIVED = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if (viewType == MESSAGE_TYPE_SENT) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_sent, parent, false)
            SentMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_received, parent, false)
            ReceivedMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int = messages.size

    override fun getItemViewType(position: Int): Int {
        // Placeholder logic: even index messages are sent, odd are received
        return if (position % 2 == 0) {
            MESSAGE_TYPE_SENT
        } else {
            MESSAGE_TYPE_RECEIVED
        }
    }

    abstract class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(message: String)
    }

    class SentMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.text_message_body)
        override fun bind(message: String) {
            messageText.text = message
        }
    }

    class ReceivedMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.text_message_body)
        override fun bind(message: String) {
            messageText.text = message
        }
    }
}