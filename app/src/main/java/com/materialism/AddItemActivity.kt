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
  private lateinit var databaseManager: DatabaseManager

  private val THUMBNAIL_SIZE = 480
  private var imageUri = ""

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.add_item_activity)

    // Registers a photo picker activity launcher in single-select mode.
    val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
          val thumbnail = findViewById<View>(R.id.image_thumbnail) as ImageView
          // Callback is invoked after the user selects a media item or closes the
          // photo picker.
          if (uri != null) {
            imageUri = uri.toString()
            val bitmap = getThumbnail(uri)
            thumbnail.setImageBitmap(bitmap)
          }
        }

    val uploadFilesButton = findViewById<Button>(R.id.uploadFiles)
    val addItemButton = findViewById<Button>(R.id.addItem)
    val backButton = findViewById<Button>(R.id.back)

    uploadFilesButton.setOnClickListener(
        View.OnClickListener {
          pickMedia.launch(
              PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        })

    addItemButton.setOnClickListener {
      var itemName: EditText = findViewById(R.id.itemName)
      var itemDescription: EditText = findViewById(R.id.itemDescription)
      var currentDate = LocalDate.now().toString()

      try {
        databaseManager.addItem(itemName.toString(), imageUri, itemDescription.toString(), null, false, false, currentDate, currentDate, 0, 0, null)
      } catch (e: SQLException) {
        val errorText = findViewById<View>(R.id.errorText) as TextView
        errorText.text = e.toString();
      }

      // send back to main page
      val intent = Intent(this, MainPageActivity::class.java)
      startActivity(intent)
    }

    backButton.setOnClickListener(
        View.OnClickListener {
          val intent = Intent(this, MainPageActivity::class.java)

          startActivity(intent)
        })
  }

  @Throws(FileNotFoundException::class, IOException::class)
  fun getThumbnail(uri: Uri?): Bitmap? {
    var input = this.contentResolver.openInputStream(uri!!)
    val onlyBoundsOptions = BitmapFactory.Options()
    onlyBoundsOptions.inJustDecodeBounds = true
    onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 // optional
    BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
    input!!.close()
    if (onlyBoundsOptions.outWidth == -1 || onlyBoundsOptions.outHeight == -1) {
      return null
    }
    val originalSize =
        if (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) onlyBoundsOptions.outHeight
        else onlyBoundsOptions.outWidth
    val ratio = if (originalSize > THUMBNAIL_SIZE) originalSize / THUMBNAIL_SIZE else 1.0
    val bitmapOptions = BitmapFactory.Options()
    bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio)
    bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //
    input = this.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
    input!!.close()
    return bitmap
  }

  private fun getPowerOfTwoForSampleRatio(ratio: Number): Int {
    val k = ratio.toInt()
    return if (k == 0) 1 else k
  }
}