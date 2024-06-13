package com.materialism

import android.content.Intent
import android.database.SQLException
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Spinner
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.materialism.databinding.ActivityAddItemBinding
import com.materialism.utils.DrawerUtils
import com.materialism.utils.ImageRenderer
import java.time.LocalDate

class AddItemActivity : AppCompatActivity() {

  private lateinit var binding: ActivityAddItemBinding
  private lateinit var drawerLayout: DrawerLayout
  private lateinit var navView: NavigationView

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

    drawerLayout = findViewById(R.id.drawer_layout)
    navView = findViewById(R.id.nav_view)

    DrawerUtils.setupDrawerContent(this, navView, drawerLayout)

    binding.backButton.setOnClickListener { finish() }

    binding.menuButton.setOnClickListener { DrawerUtils.openDrawer(drawerLayout) }

    // Registers a photo picker activity launcher in single-select mode.
    val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
          val thumbnail = findViewById<View>(R.id.image_placeholder) as ImageView
          // Callback is invoked after the user selects a media item or closes the
          // photo picker.
          if (uri != null) {
            imageUri = uri.toString()
            val bitmap = imageRenderer.getThumbnail(uri, THUMBNAIL_SIZE)
            thumbnail.setImageBitmap(bitmap)
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
    val isPublic =
        findViewById<RadioButton>(R.id.private_no_button).isChecked // not private == public
    val currentDate = LocalDate.now().toString()

    val categoryName = findViewById<Spinner>(R.id.category_spinner).getSelectedItem().toString()
    val categoryId = getCategoriesMap()[categoryName]

    try {
      if (categoryId != null) {
        databaseManager.addItem(
            itemName,
            imageUri,
            itemDescription,
            null,
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
