package com.materialism.category

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.materialism.R
import com.materialism.adapter.CategoryAdapter
import com.materialism.database.localDatabase.DatabaseManager
import com.materialism.utils.DrawerUtils

class ViewCategoriesActivity : ComponentActivity() {

  private lateinit var recyclerView: RecyclerView
  private lateinit var categoryAdapter: CategoryAdapter
  private var databaseManager = DatabaseManager(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_view_categories)
    databaseManager.open()

    val menuIcon: ImageButton = findViewById(R.id.ic_menu)
    DrawerUtils.setupPopupMenu(this, menuIcon)

    val spinnerCategoryType = findViewById<Spinner>(R.id.spinner_category_type)
    recyclerView = findViewById(R.id.recycler_view)

    recyclerView.layoutManager = LinearLayoutManager(this)
    categoryAdapter = CategoryAdapter()
    recyclerView.adapter = categoryAdapter

    val categoryTypes = arrayOf("Categories", "Subcategories")
    val spinnerAdapter = ArrayAdapter(this, R.layout.custom_spinner_item, categoryTypes)
    spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_item)
    spinnerCategoryType.adapter = spinnerAdapter

    // Set dropdown width and height
    spinnerCategoryType.post {
      try {
        val popup = Spinner::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }

    spinnerCategoryType.onItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
          override fun onItemSelected(
              parent: AdapterView<*>?,
              view: View?,
              position: Int,
              id: Long
          ) {
            when (position) {
              0 -> loadCategories()
              1 -> loadSubcategories()
            }
          }

          override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
  }

  private fun loadCategories() {
    val categoriesCursor = databaseManager.getAllCategories()
    val categoryArray = ArrayList<String>()

    if (categoriesCursor.moveToFirst()) {
      do {
        val key = categoriesCursor.getString(categoriesCursor.getColumnIndexOrThrow("name"))
        categoryArray.add(key)
      } while (categoriesCursor.moveToNext())
      categoriesCursor.close()
    }

    categoryAdapter.updateData(categoryArray)
  }

  private fun loadSubcategories() {
    val subCategoriesCursor = databaseManager.getAllSubcategories()
    val subCategoryArray = ArrayList<String>()

    if (subCategoriesCursor.moveToFirst()) {
      do {
        val key = subCategoriesCursor.getString(subCategoriesCursor.getColumnIndexOrThrow("name"))
        subCategoryArray.add(key)
      } while (subCategoriesCursor.moveToNext())
      subCategoriesCursor.close()
    }

    categoryAdapter.updateData(subCategoryArray)
  }
}
