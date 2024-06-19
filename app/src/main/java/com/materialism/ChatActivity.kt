package com.materialism

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.materialism.utils.DrawerUtils

class ChatActivity : AppCompatActivity() {

  private lateinit var messageInput: EditText
  private lateinit var sendButton: ImageButton
  private lateinit var recyclerView: RecyclerView
  private lateinit var chatAdapter: ChatAdapter
  private lateinit var messagesList: MutableList<String>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_chat)

    val menuIcon: ImageButton = findViewById(R.id.ic_menu)
    DrawerUtils.setupPopupMenu(this, menuIcon)

    messageInput = findViewById(R.id.message_input)
    sendButton = findViewById(R.id.send_button)
    recyclerView = findViewById(R.id.recycler_view)

    messagesList = mutableListOf()
    chatAdapter = ChatAdapter(messagesList)

    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.adapter = chatAdapter

    sendButton.setOnClickListener {
      val message = messageInput.text.toString()
      if (message.isNotEmpty()) {
        messagesList.add(message)
        chatAdapter.notifyItemInserted(messagesList.size - 1)
        messageInput.text.clear()
        recyclerView.scrollToPosition(messagesList.size - 1)
      }
    }
  }
}
