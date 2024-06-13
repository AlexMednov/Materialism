package com.materialism

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.materialism.databinding.ActivityViewItemsBinding
import com.materialism.sampledata.Item
import com.materialism.utils.DrawerUtils
import com.materialism.utils.ImageRenderer

class ViewItemsActivity : AppCompatActivity() {

  private lateinit var binding: ActivityViewItemsBinding
  private lateinit var itemAdapter: ItemAdapter
  private lateinit var drawerLayout: DrawerLayout
  private lateinit var navView: NavigationView
  private var databaseManager = DatabaseManager(this)

  private var imageRenderer = ImageRenderer(this.contentResolver)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityViewItemsBinding.inflate(layoutInflater)
    setContentView(binding.root)
    databaseManager.open()

    drawerLayout = findViewById(R.id.drawer_layout)
    navView = findViewById(R.id.nav_view)

    DrawerUtils.setupDrawerContent(this, navView, drawerLayout)
    setupSortSpinner()
    setupRecyclerView()

    binding.menuIcon.setOnClickListener { DrawerUtils.openDrawer(drawerLayout) }

    binding.backButton.setOnClickListener { onBackPressed() }
  }

  private fun setupSortSpinner() {
    val sortOptions =
        arrayOf(
            "Sort by Category",
            "Sort by Name Ascending",
            "Sort by Name Descending",
            "Sort by Location Ascending",
            "Sort by Location Descending",
            "Sort by Date Ascending",
            "Sort by Date Descending",
        )
    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sortOptions)
    binding.sortSpinner.adapter = adapter

    binding.sortSpinner.onItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
          override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            when (position) {
              0 -> itemAdapter.sortByCategory()
              1 -> itemAdapter.sortByNameAsc()
              2 -> itemAdapter.sortByNameDesc()
              3 -> itemAdapter.sortByLocationAsc()
              4 -> itemAdapter.sortByLocationDesc()
              5 -> itemAdapter.sortByDateAsc()
              6 -> itemAdapter.sortByDateDesc()
            }
          }

          override fun onNothingSelected(parent: AdapterView<*>) {}
        }
  }

  private fun setupRecyclerView() {
    itemAdapter = ItemAdapter(getAllItems(), true, imageRenderer) // Pass true to show edit button
    binding.recyclerView.apply {
      layoutManager = LinearLayoutManager(this@ViewItemsActivity)
      adapter = itemAdapter
    }
  }

  private fun getAllItems(): ArrayList<Item> {
    val itemsCursor = databaseManager.getAllItems()
    val itemsArray = ArrayList<Item>()

    if (itemsCursor.moveToFirst()) {
      do {
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

        val item = Item(itemName, itemDescription, imageUri, categoryName, itemLocation, itemDateTimeAdded)

        itemsArray.add(item)
      } while (itemsCursor.moveToNext())
      itemsCursor.close()
    }
    return itemsArray
  }
}
