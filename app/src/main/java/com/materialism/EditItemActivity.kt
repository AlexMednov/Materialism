package com.materialism

import android.content.ContentResolver
import android.content.Intent
import android.database.SQLException
import android.graphics.Bitmap
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
import androidx.core.net.toUri
import com.materialism.databinding.ActivityEditItemBinding
import com.materialism.sampledata.Item
import com.materialism.utils.DrawerUtils
import com.materialism.utils.ImageRenderer
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.LocalDate

class EditItemActivity : AppCompatActivity() {

  private lateinit var binding: ActivityEditItemBinding
  private var databaseManager = DatabaseManager(this)
  private lateinit var imageRenderer: ImageRenderer
  private val THUMBNAIL_SIZE = 480
  private var newImageUri = ""

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityEditItemBinding.inflate(layoutInflater)
    setContentView(binding.root)
    imageRenderer = ImageRenderer(this.contentResolver)
    databaseManager.open()

    val menuIcon: ImageButton = findViewById(R.id.ic_menu)
    DrawerUtils.setupPopupMenu(this, menuIcon)

    setFields()

    binding.backButton.setOnClickListener { finish() }

    // Registers a photo picker activity launcher in single-select mode.
    val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
          // Callback is invoked after the user selects a media item or closes the
          // photo picker.
          if (uri != null) {
            setImage(uri)
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

    binding.editItemButton.setOnClickListener { updateItem() }
  }

  private fun setImage(uri: Uri) {
    newImageUri = uri.toString()
    val bitmap = imageRenderer.getThumbnail(uri, THUMBNAIL_SIZE)
    binding.imagePlaceholder.setImageBitmap(bitmap)

    val newUri = copyUriToPictures(uri)
    if (newUri != null) {
      newImageUri = newUri.toString()
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

  private fun setFields() {
    val it = getItemFromIntent()

    newImageUri = it.imageUri

    try {
      binding.imagePlaceholder.setImageBitmap(
        imageRenderer.getThumbnail(it.imageUri.toUri(), THUMBNAIL_SIZE))
    } catch (e: Exception) {
      Log.e("ImageRenderer", e.toString())
    }

    binding.nameEditText.setText(it.name)
    binding.descriptionEditText.setText(it.description)
    binding.locationEditText.setText(it.location)

    binding.categorySpinner.setSelection(getCategoryPosition(it.category))
  }

  private fun getItemFromIntent(): Item {
    var imageUri = ""
    var name = ""
    var description = ""
    var category = ""
    var location = ""
    var date = ""

    imageUri = intent.getStringExtra("imageUri").toString()
    name = intent.getStringExtra("name").toString()
    description = intent.getStringExtra("description").toString()
    category = intent.getStringExtra("category").toString()
    location = intent.getStringExtra("location").toString().substringAfter(" ")
    date = intent.getStringExtra("date").toString()

    return Item(name, description, imageUri, category, location, date)
  }

  private fun getCategoryPosition(categoryName: String): Int {
    return getCategoriesList().indexOf(categoryName)
  }

  private fun getCategoriesList(): ArrayList<String> {
    val categoriesCursor = databaseManager.getAllCategories()
    val categoryList = ArrayList<String>()

    if (categoriesCursor.moveToFirst()) {
      do {
        val name = categoriesCursor.getString(categoriesCursor.getColumnIndexOrThrow("name"))
        categoryList.add(name)
      } while (categoriesCursor.moveToNext())
      categoriesCursor.close()
    }

    return categoryList
  }

  private fun updateItem() {
    val itemId = intent.getIntExtra("itemId", 0)
    val itemName: String = findViewById<EditText>(R.id.name_edit_text).text.toString()
    val itemDescription: String = findViewById<EditText>(R.id.description_edit_text).text.toString()
    val itemLocation: String = findViewById<EditText>(R.id.location_edit_text).text.toString()
    val isPublic =
        findViewById<RadioButton>(R.id.private_no_button).isChecked // not private == public
    val dateAdded = intent.getStringExtra("date")?.substringAfter(" ")
    val currentDate = LocalDate.now().toString()

    val categoryName = findViewById<Spinner>(R.id.category_spinner).selectedItem.toString()
    val categoryId = getCategoriesMap()[categoryName]

    if (newImageUri == "") {
      newImageUri = intent.getStringExtra("imageUri").toString()
    }

    try {
      if (categoryId != null) {
        if (dateAdded != null) {
          databaseManager.updateItem(
            itemId,
            itemName,
            newImageUri,
            itemDescription,
            itemLocation,
            isPublic,
            false,
            dateAdded,
            currentDate,
            0,
            categoryId,
            null)
        }
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
}
