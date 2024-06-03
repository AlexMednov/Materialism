package com.materialism

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.materialism.databinding.DesignMainPageActivityBinding
import com.materialism.sampledata.Item
import com.materialism.utils.DrawerUtils

class DesignMainPageActivity : AppCompatActivity() {

  private lateinit var binding: DesignMainPageActivityBinding
  private lateinit var drawerLayout: DrawerLayout
  private lateinit var navView: NavigationView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DesignMainPageActivityBinding.inflate(layoutInflater)
    setContentView(binding.root)

    drawerLayout = findViewById(R.id.drawer_layout)
    navView = findViewById(R.id.nav_view)

    DrawerUtils.setupDrawerContent(this, navView, drawerLayout)

    binding.level.text = "Level: 1"
    binding.progressBar.progress = 10
    binding.progressBar.max = 20
    binding.exp.text = "Exp: 10/20"

    val items =
      listOf(
        Item("Item 1", "Description 1", "Category 1", "Location 1", "Date 1"),
        Item("Item 2", "Description 2", "Category 2", "Location 2", "Date 2"))

    binding.recyclerView.layoutManager = LinearLayoutManager(this)
    binding.recyclerView.adapter = ItemAdapter(items, false)

    binding.fab.setOnClickListener {
      val intent = Intent(this, AddItemActivity::class.java)
      startActivity(intent)
    }

    binding.libraryIcon.setOnClickListener { openViewItemsActivity(it) }

    binding.icMenu.setOnClickListener { DrawerUtils.openDrawer(drawerLayout) }
  }

  // Method to handle the click event for the library icon
  fun openViewItemsActivity(view: View) {
    val intent = Intent(this, ViewItemsActivity::class.java)
    startActivity(intent)
  }

  // Method to handle the click event for the people icon
  fun openViewFriendsActivity(view: View) {
    val intent = Intent(this, ViewFriendsActivity::class.java)
    startActivity(intent)
  }
}
