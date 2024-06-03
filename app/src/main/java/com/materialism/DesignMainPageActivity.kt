package com.materialism

import android.content.Intent
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
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

    val items = listOf(
      Item("Item 1", "Description 1", "Category 1", "Location 1", "Date 1"),
      Item("Item 2", "Description 2", "Category 2", "Location 2", "Date 2")
    )

    binding.recyclerView.layoutManager = LinearLayoutManager(this)
    binding.recyclerView.adapter = ItemAdapter(items, false)

    binding.fab.setOnClickListener {
      showFabOptionsMenu(it)
    }

    binding.libraryIcon.setOnClickListener { openViewItemsActivity(it) }
    binding.icMenu.setOnClickListener { DrawerUtils.openDrawer(drawerLayout) }

    val icFlag = findViewById<ImageButton>(R.id.ic_flag)
    icFlag.setOnClickListener {
      val intent = Intent(this, RequestActivity::class.java)
      startActivity(intent)
    }
  }

  private fun showFabOptionsMenu(view: View) {
    val popup = PopupMenu(this, view)
    val inflater: MenuInflater = popup.menuInflater
    inflater.inflate(R.menu.menu_fab_options, popup.menu)
    popup.setOnMenuItemClickListener { item: MenuItem ->
      when (item.itemId) {
        R.id.action_add_item -> {
          val intent = Intent(this, AddItemActivity::class.java)
          startActivity(intent)
          true
        }
        R.id.action_add_category -> {
          val intent = Intent(this, AddCategoryActivity::class.java)
          startActivity(intent)
          true
        }
        R.id.action_add_subcategory -> {
          val intent = Intent(this, AddSubcategoryActivity::class.java)
          startActivity(intent)
          true
        }
        else -> false
      }
    }
    popup.show()
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
