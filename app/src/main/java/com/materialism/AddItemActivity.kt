package com.materialism

import android.content.ContentResolver
import android.content.Intent
import android.database.SQLException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Spinner
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.materialism.databinding.ActivityAddItemBinding
import com.materialism.utils.DrawerUtils
import com.materialism.utils.ImageRenderer
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.time.LocalDate

class AddItemActivity : AppCompatActivity() {

  private lateinit var binding: ActivityAddItemBinding
  private var databaseManager = DatabaseManager(this)
  private lateinit var imageRenderer: ImageRenderer
  private val THUMBNAIL_SIZE = 480
  private var imageUri = ""

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityAddItemBinding.inflate(layoutInflater)
    setContentView(binding.root)
    imageRenderer = ImageRenderer(this.contentResolver)
    databaseManager.open()

    val menuIcon: ImageButton = findViewById(R.id.menu_button)
    DrawerUtils.setupPopupMenu(this, menuIcon)

    binding.backButton.setOnClickListener { finish() }

    // Registers a photo picker activity launcher in single-select mode.
    val pickMedia =
      registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        val thumbnail: ImageView = findViewById(R.id.image_placeholder)
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
          imageUri = uri.toString()
          val bitmap = imageRenderer.getThumbnail(uri, THUMBNAIL_SIZE)
          thumbnail.setImageBitmap(bitmap)

          val newUri = copyUriToPictures(uri)
          if (newUri != null) {
            imageUri = newUri.toString()
          } else {
            Log.e("FileCopy", "Failed to copy file to Pictures directory")
          }

          // Persist the permission to access the URI
          val contentResolver = applicationContext.contentResolver
          val takeFlags: Int =
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
          try {
            contentResolver.takePersistableUriPermission(uri, takeFlags)
          } catch (e: SecurityException) {
            Log.e("PersistURI", "No persistable permission grants found for URI: $uri", e)
          }
        }
      }

    binding.selectPictureButton.setOnClickListener {
      pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    binding.takePictureButton.setOnClickListener {
      val intent = Intent(this, CameraActivity::class.java)
      startActivity(intent)
    }

    val adapter =
      ArrayAdapter(this, android.R.layout.simple_spinner_item, getCategoriesForSpinner())
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    binding.categorySpinner.adapter = adapter

    binding.addItemButton.setOnClickListener { addItem() }
  }

  private fun addItem() {
    val itemName: String = findViewById<EditText>(R.id.name_edit_text).text.toString()
    val itemDescription: String = findViewById<EditText>(R.id.description_edit_text).text.toString()
    val itemLocation: String = findViewById<EditText>(R.id.location_edit_text).text.toString()
    val isPublic =
      findViewById<RadioButton>(R.id.private_no_button).isChecked // not private == public
    val currentDate = LocalDate.now().toString()

    val categoryName = findViewById<Spinner>(R.id.category_spinner).selectedItem.toString()
    val categoryId = getCategoriesMap()[categoryName]

    try {
      if (categoryId != null) {
        databaseManager.addItem(
            itemName,
            imageUri,
            itemDescription,
            itemLocation,
            isPublic,
            false,
            currentDate,
            currentDate,
            0,
            categoryId,
            null)
      }
    } catch (e: SQLException) {
      Log.e("SQLException", e.toString())
    }

    // send to previous page
    val intent = Intent(this, MainPageActivity::class.java)
    startActivity(intent)
  }

  private fun copyUriToPictures(uri: Uri): Uri? {
    val contentResolver: ContentResolver = applicationContext.contentResolver
    val picturesDir =
        ContextCompat.getExternalFilesDirs(applicationContext, Environment.DIRECTORY_PICTURES)[0]
    val fileName = "image_${System.currentTimeMillis()}.jpg"
    val inputStream: InputStream? = contentResolver.openInputStream(uri)
    val file = File(picturesDir, fileName)

    return try {
      val outputStream = FileOutputStream(file)
      inputStream?.copyTo(outputStream)
      outputStream.close()
      inputStream?.close()
      Uri.fromFile(file)
    } catch (e: Exception) {
      Log.e("FileCopy", "Error copying file: ${e.message}", e)
      null
    }
  }

  private fun getCategoriesForSpinner(): ArrayList<String> {
    val categoriesCursor = databaseManager.getAllCategories()
    val categoryArray = ArrayList<String>()
    categoryArray.add("< Select a category >")

    if (categoriesCursor.moveToFirst()) {
      do {
        val name = categoriesCursor.getString(categoriesCursor.getColumnIndexOrThrow("name"))
        categoryArray.add(name)
      } while (categoriesCursor.moveToNext())
      categoriesCursor.close()
    }

    return categoryArray
  }

  private fun getCategoriesMap(): Map<String, Int> {
    val categoriesCursor = databaseManager.getAllCategories()
    val categoryMap = mutableMapOf<String, Int>()

    if (categoriesCursor.moveToFirst()) {
      do {
        val name = categoriesCursor.getString(categoriesCursor.getColumnIndexOrThrow("name"))
        val id = categoriesCursor.getInt(categoriesCursor.getColumnIndexOrThrow("id"))
        categoryMap[name] = id
      } while (categoriesCursor.moveToNext())
      categoriesCursor.close()
    }

    return categoryMap
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
