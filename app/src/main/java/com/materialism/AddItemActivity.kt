package com.materialism

import android.content.Intent
import android.database.SQLException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import java.io.FileNotFoundException
import java.io.IOException
import java.time.LocalDate

class AddItemActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.add_item_activity)

    // Registers a photo picker activity launcher in single-select mode.


    val uploadFilesButton = findViewById<Button>(R.id.upload_files)
    val addItemButton = findViewById<Button>(R.id.add_item)
    val backButton = findViewById<Button>(R.id.back)




  }

}
