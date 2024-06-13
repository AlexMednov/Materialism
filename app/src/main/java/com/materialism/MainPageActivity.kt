package com.materialism

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.materialism.databinding.MainPageActivityBinding
import com.materialism.sampledata.Item
import com.materialism.utils.DrawerUtils

class MainPageActivity : AppCompatActivity() {

  private lateinit var binding: MainPageActivityBinding
  private lateinit var drawerLayout: DrawerLayout
  private lateinit var navView: NavigationView
  private var databaseManager = DatabaseManager(this)
  private var databaseAdapter = DatabaseAdapter(databaseManager)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = MainPageActivityBinding.inflate(layoutInflater)
    setContentView(binding.root)
    databaseManager.open()

    drawerLayout = findViewById(R.id.drawer_layout)
    navView = findViewById(R.id.nav_view)

    databaseAdapter.databaseManager.open()
    databaseAdapter.syncCategories()
    databaseAdapter.syncSubCategories()
    databaseAdapter.syncQuests()
    databaseAdapter.syncQuestItems()

    var exampleFriendUserId = 2011884291
    var loggedInUserId = 1 //dummy logged in userId

    databaseAdapter.getFriendsUserIds(loggedInUserId) { friendsUserIds ->

    }
    //val categoryButton = findViewById<Button>(R.id.category_button)

    DrawerUtils.setupDrawerContent(this, navView, drawerLayout)

    binding.level.text = "Level: 1"
    binding.progressBar.progress = 10
    binding.progressBar.max = 20
    binding.exp.text = "Exp: 10/20"

    val items = getAllItems()

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

  private fun getAllItems(): ArrayList<Item> {
    val itemsCursor = databaseManager.getAllItems()
    val itemsArray = ArrayList<Item>()

    if (itemsCursor.moveToFirst()) {
      do {
        val itemName = itemsCursor.getString(itemsCursor.getColumnIndexOrThrow("name"))
        val itemDescription = itemsCursor.getString(itemsCursor.getColumnIndexOrThrow("description"))
        val itemCategoryId = itemsCursor.getInt(itemsCursor.getColumnIndexOrThrow("categoryId"))
        val itemLocation = "Location: " + itemsCursor.getString(itemsCursor.getColumnIndexOrThrow("location"))
        val itemDateTimeAdded = "Added: " + itemsCursor.getString(itemsCursor.getColumnIndexOrThrow("dateTimeAdded"))

        val categoryCursor = databaseManager.getCategory(itemCategoryId)
        var categoryName = "Category: "
        if (categoryCursor.moveToFirst()) {
          val colIndex = categoryCursor.getColumnIndexOrThrow("name")
          if (colIndex != -1) {
            categoryName += categoryCursor.getString(colIndex)
          }
        }

        val item = Item(itemName, itemDescription, categoryName, itemLocation, itemDateTimeAdded)

        itemsArray.add(item)
      } while (itemsCursor.moveToNext())
      itemsCursor.close()
    }
    return itemsArray
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
    val intent = Intent(this, AddCategoryActivity::class.java)
    startActivity(intent)
  }
}
