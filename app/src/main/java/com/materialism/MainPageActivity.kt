package com.materialism

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

class MainPageActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_page_activity)

    val addItemButton = findViewById<Button>(R.id.addItems)
    val backButton = findViewById<Button>(R.id.back)

    addItemButton.setOnClickListener(
        View.OnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)

            startActivity(intent)
        })

    backButton.setOnClickListener(
        View.OnClickListener {
          val intent = Intent(this, LoginActivity::class.java)

          startActivity(intent)
        })
  }
}
