package com.materialism

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.materialism.databinding.ActivityAddItemBinding

class DesignAddItemActivity : AppCompatActivity() {

  private lateinit var binding: ActivityAddItemBinding
  private lateinit var drawerLayout: DrawerLayout
  private lateinit var navView: NavigationView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityAddItemBinding.inflate(layoutInflater)
    setContentView(binding.root)

    drawerLayout = findViewById(R.id.drawer_layout)
    navView = findViewById(R.id.nav_view)

    DrawerUtils.setupDrawerContent(this, navView, drawerLayout)

    binding.backButton.setOnClickListener { onBackPressed() }

    binding.menuButton.setOnClickListener { DrawerUtils.openDrawer(drawerLayout) }

    val categories = resources.getStringArray(R.array.categories_array)
    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    binding.categorySpinner.adapter = adapter

    binding.selectPictureButton.setOnClickListener {}

    binding.addItemButton.setOnClickListener {}
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
  }
}
