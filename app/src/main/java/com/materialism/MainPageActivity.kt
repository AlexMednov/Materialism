package com.materialism

import android.content.Intent
import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.materialism.adapter.DatabaseAdapter
import com.materialism.adapter.ItemAdapter
import com.materialism.category.AddCategoryActivity
import com.materialism.category.AddSubcategoryActivity
import com.materialism.database.localDatabase.DatabaseManager
import com.materialism.databinding.MainPageActivityBinding
import com.materialism.dto.Item
import com.materialism.friend.RequestItemActivity
import com.materialism.friend.ViewFriendsActivity
import com.materialism.item.AddItemActivity
import com.materialism.item.ViewItemsActivity
import com.materialism.item.ViewSingleItemActivity
import com.materialism.utils.DrawerUtils
import com.materialism.utils.ImageRenderer

class MainPageActivity : AppCompatActivity() {

  private lateinit var binding: MainPageActivityBinding
  private var databaseManager = DatabaseManager(this)
  private var databaseAdapter = DatabaseAdapter(databaseManager)

  private lateinit var imageRenderer: ImageRenderer

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = MainPageActivityBinding.inflate(layoutInflater)
    setContentView(binding.root)
    imageRenderer = ImageRenderer(this.contentResolver)
    databaseManager.open()

    databaseAdapter.databaseManager.open()
    databaseAdapter.syncCategories()
    databaseAdapter.syncSubCategories()
    databaseAdapter.syncQuests()
    databaseAdapter.syncQuestItems()

    val menuIcon: ImageButton = findViewById(R.id.ic_menu)
    menuIcon.setOnClickListener { DrawerUtils.setupPopupMenu(this, menuIcon) }

    binding.level.text = "Level: 1"
    binding.progressBar.progress = 10
    binding.progressBar.max = 20
    binding.exp.text = "Exp: 10/20"

    val items = getAllItems()

    binding.recyclerView.layoutManager = LinearLayoutManager(this)
    val itemAdapter = ItemAdapter(items, false, imageRenderer)
    binding.recyclerView.adapter = itemAdapter
    itemAdapter.setOnClickListener(
        object : ItemAdapter.OnClickListener {
          override fun onClick(position: Int, itemModel: Item) {
            openViewSingleItemActivity(position, itemModel)
          }
        })

    binding.fab.setOnClickListener { showFabOptionsMenu(it) }

    binding.libraryIcon.setOnClickListener { openViewItemsActivity(it) }

    val icFlag = findViewById<ImageButton>(R.id.ic_history)
    icFlag.setOnClickListener {
      val intent = Intent(this, RequestItemActivity::class.java)
      startActivity(intent)
    }
  }

  private fun getAllItems(): List<Item> {
    val itemsCursor = databaseManager.getAllItems()
    val itemsList = ArrayList<Item>()

    if (itemsCursor.moveToFirst()) {
      do {
        val itemId = itemsCursor.getInt(itemsCursor.getColumnIndexOrThrow("id"))
        val itemName = itemsCursor.getString(itemsCursor.getColumnIndexOrThrow("name"))
        val itemDescription =
            itemsCursor.getString(itemsCursor.getColumnIndexOrThrow("description"))
        val imageUri = itemsCursor.getString(itemsCursor.getColumnIndexOrThrow("imageURI"))
        val itemCategoryId = itemsCursor.getInt(itemsCursor.getColumnIndexOrThrow("categoryId"))
        val itemLocation =
            "Location: " + itemsCursor.getString(itemsCursor.getColumnIndexOrThrow("location"))
        val itemDateTimeAdded =
            "Added: " + itemsCursor.getString(itemsCursor.getColumnIndexOrThrow("dateTimeAdded"))

        val categoryCursor = databaseManager.getCategory(itemCategoryId)
        var categoryName = "Category: "
        if (categoryCursor.moveToFirst()) {
          val colIndex = categoryCursor.getColumnIndexOrThrow("name")
          if (colIndex != -1) {
            categoryName += categoryCursor.getString(colIndex)
          }
        }

        val item =
            Item(
                itemName,
                itemDescription,
                imageUri,
                categoryName,
                itemLocation,
                itemDateTimeAdded,
                itemId)

        itemsList.add(item)
      } while (itemsCursor.moveToNext())
      itemsCursor.close()
    }
    return itemsList
  }

  private fun showFabOptionsMenu(view: View) {
    val popup = PopupMenu(this, view)
    val inflater: MenuInflater = popup.menuInflater
    inflater.inflate(R.menu.menu_fab_options, popup.menu)
    popup.setOnMenuItemClickListener { item ->
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
    val intent = Intent(this, ViewFriendsActivity::class.java)
    startActivity(intent)
  }

  // Method to handle the click event for the recycle list
  fun openViewSingleItemActivity(position: Int, itemModel: Item) {
    val intent = Intent(this, ViewSingleItemActivity::class.java)

    intent.putExtra("itemId", itemModel.id)
    intent.putExtra("imageUri", itemModel.imageUri)
    intent.putExtra("name", itemModel.name)
    intent.putExtra("description", itemModel.description)
    intent.putExtra("category", itemModel.category)
    intent.putExtra("location", itemModel.location)
    intent.putExtra("date", itemModel.date)
    startActivity(intent)
  }
}
