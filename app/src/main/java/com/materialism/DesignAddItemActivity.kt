package com.materialism

import android.content.Intent
import android.database.SQLException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.materialism.databinding.ActivityAddItemBinding
import java.io.FileNotFoundException
import java.io.IOException
import java.time.LocalDate

class DesignAddItemActivity : AppCompatActivity() {

  private lateinit var binding: ActivityAddItemBinding
  private lateinit var drawerLayout: DrawerLayout
  private lateinit var navView: NavigationView

  private lateinit var databaseManager: DatabaseManager
  private val THUMBNAIL_SIZE = 480
  private var imageUri = ""

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityAddItemBinding.inflate(layoutInflater)
    setContentView(binding.root)

    drawerLayout = findViewById(R.id.drawer_layout)
    navView = findViewById(R.id.nav_view)

    DrawerUtils.setupDrawerContent(this, navView, drawerLayout)

    binding.backButton.setOnClickListener {
      
    }

    binding.menuButton.setOnClickListener { DrawerUtils.openDrawer(drawerLayout) }

    val categories = resources.getStringArray(R.array.categories_array)
    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    binding.categorySpinner.adapter = adapter

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

    binding.selectPictureButton.setOnClickListener {
      View.OnClickListener {
        pickMedia.launch(
          PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
      }
    }

    binding.addItemButton.setOnClickListener {
      var itemName: EditText = findViewById(R.id.item_name)
      var itemDescription: EditText = findViewById(R.id.item_description)
      var currentDate = LocalDate.now().toString()

      try {
        databaseManager.addItem(
          itemName.toString(),
          imageUri,
          itemDescription.toString(),
          null,
          false,
          false,
          currentDate,
          currentDate,
          0,
          0,
          null)
      } catch (e: SQLException) {
        val errorText = findViewById<View>(R.id.error_text) as TextView
        errorText.text = e.toString()
      }

      // send back to main page
      val intent = Intent(this, MainPageActivity::class.java)
      startActivity(intent)
    }
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
