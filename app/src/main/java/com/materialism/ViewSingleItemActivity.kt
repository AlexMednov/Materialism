package com.materialism

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.materialism.databinding.ActivityAddItemBinding
import com.materialism.databinding.ActivityViewSingleItemBinding
import com.materialism.sampledata.Item
import com.materialism.utils.DrawerUtils
import com.materialism.utils.ImageRenderer

class ViewSingleItemActivity : AppCompatActivity() {

  private lateinit var binding: ActivityViewSingleItemBinding
  private lateinit var drawerLayout: DrawerLayout
  private lateinit var navView: NavigationView

  private var databaseManager = DatabaseManager(this)
  private lateinit var imageRenderer: ImageRenderer
  private val THUMBNAIL_SIZE = 480
  private var imageUri = ""

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityViewSingleItemBinding.inflate(layoutInflater)
    setContentView(binding.root)
    imageRenderer = ImageRenderer(this.contentResolver)
    databaseManager.open()

    drawerLayout = findViewById(R.id.drawer_layout)
    navView = findViewById(R.id.nav_view)

    DrawerUtils.setupDrawerContent(this, navView, drawerLayout)

    binding.backButton.setOnClickListener { finish() }

    binding.menuButton.setOnClickListener { DrawerUtils.openDrawer(drawerLayout) }

    val it = getItemFromIntent()
    binding.imagePlaceholder.setImageBitmap(imageRenderer.getThumbnail(it.imageUri.toUri(), THUMBNAIL_SIZE))
    binding.nameText.text = it.name
    binding.descriptionText.text = it.name
    binding.locationText.text = it.name
    binding.categoryText.text = it.name
    binding.nameText.text = it.name

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
