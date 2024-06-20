package com.materialism.item

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.materialism.R
import com.materialism.database.localDatabase.DatabaseManager
import com.materialism.databinding.ActivityViewSingleItemBinding
import com.materialism.dto.Item
import com.materialism.utils.DrawerUtils
import com.materialism.utils.ImageRenderer

class ViewSingleItemActivity : AppCompatActivity() {

  private lateinit var binding: ActivityViewSingleItemBinding

  private var databaseManager = DatabaseManager(this)
  private lateinit var imageRenderer: ImageRenderer
  private val THUMBNAIL_SIZE = 480

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityViewSingleItemBinding.inflate(layoutInflater)
    setContentView(binding.root)
    imageRenderer = ImageRenderer(this.contentResolver)
    databaseManager.open()

    val menuIcon: ImageButton = findViewById(R.id.ic_menu)
    DrawerUtils.setupPopupMenu(this, menuIcon)

    binding.backButton.setOnClickListener { finish() }

    val it = getItemFromIntent()
    try {
      binding.imagePlaceholder.setImageBitmap(
          imageRenderer.getThumbnail(it.imageUri.toUri(), THUMBNAIL_SIZE))
    } catch (e: Exception) {
      Log.e("ImageRenderer", e.toString())
    }

    binding.nameText.text = it.name
    binding.descriptionText.text = it.description
    binding.locationText.text = it.location
    binding.categoryText.text = it.category
    binding.dateText.text = it.date
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
    location = intent.getStringExtra("location").toString()
    date = intent.getStringExtra("date").toString()

    return Item(name, description, imageUri, category, location, date)
  }
}
