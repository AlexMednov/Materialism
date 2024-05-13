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

    // Registers a photo picker activity launcher in single-select mode.
    val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
          val text = findViewById<View>(R.id.fileUri) as TextView
          // Callback is invoked after the user selects a media item or closes the
          // photo picker.
          if (uri != null) {
            text.text = uri.toString()
          } else {
            text.text = "No media selected"
          }
        }

    val uploadFilesButton = findViewById<Button>(R.id.uploadFiles)
    val backButton = findViewById<Button>(R.id.back)

    uploadFilesButton.setOnClickListener(
        View.OnClickListener {
          pickMedia.launch(
              PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        })

    backButton.setOnClickListener(
        View.OnClickListener {
          val intent = Intent(this, LoginActivity::class.java)

          startActivity(intent)
        })
  }
}
