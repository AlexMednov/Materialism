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

class MainPageActivity : AppCompatActivity() {

  private lateinit var binding: DesignMainPageActivityBinding
  private lateinit var drawerLayout: DrawerLayout
  private lateinit var navView: NavigationView
  private var databaseManager = DatabaseManager(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DesignMainPageActivityBinding.inflate(layoutInflater)
    setContentView(binding.root)
    databaseManager.open()

    drawerLayout = findViewById(R.id.drawer_layout)
    navView = findViewById(R.id.nav_view)

    DrawerUtils.setupDrawerContent(this, navView, drawerLayout)

    binding.level.text = "Level: 1"
    binding.progressBar.progress = 10
    binding.progressBar.max = 20
    binding.exp.text = "Exp: 10/20"

    val items = getAllItems()

    binding.recyclerView.layoutManager = LinearLayoutManager(this)
    binding.recyclerView.adapter = ItemAdapter(items, false)

    binding.fab.setOnClickListener {
      openViewAddItemActivity(it)
    }

    binding.libraryIcon.setOnClickListener { openViewItemsActivity(it) }

    binding.icMenu.setOnClickListener { DrawerUtils.openDrawer(drawerLayout) }
  }

  private fun getAllItems(): ArrayList<Item> {
    val itemsCursor = databaseManager.getAllItems()
    val itemsArray = ArrayList<Item>()

    if (itemsCursor.moveToFirst()) {
      do {
        val itemName = itemsCursor.getColumnIndexOrThrow("name").toString()
        val itemDescription = itemsCursor.getColumnIndexOrThrow("description").toString()
        val itemCategoryId = itemsCursor.getColumnIndexOrThrow("categoryId")
        val itemLocation = itemsCursor.getColumnIndexOrThrow("location").toString()
        val itemDateTimeAdded = itemsCursor.getColumnIndexOrThrow("dateTimeAdded").toString()

        val categoryCursor = databaseManager.getCategory(itemCategoryId)
        val categoryName = categoryCursor.getColumnIndexOrThrow("name").toString()

        itemsArray.add(Item(itemName, itemDescription, categoryName, itemLocation, itemDateTimeAdded))
      } while (itemsCursor.moveToNext())
      itemsCursor.close()
    }

    return itemsArray
  }

  // Method to handle the click event for the library icon
  private fun openViewAddItemActivity(view: View) {
    val intent = Intent(this, AddItemActivity::class.java)
    startActivity(intent)
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
