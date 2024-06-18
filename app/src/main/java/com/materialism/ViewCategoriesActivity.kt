package com.materialism

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.materialism.utils.DrawerUtils

class ViewCategoriesActivity : ComponentActivity() {

  private lateinit var recyclerView: RecyclerView
  private lateinit var categoryAdapter: CategoryAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_view_categories)

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
    val categories = listOf("Category 1", "Category 2", "Category 3")
    categoryAdapter.updateData(categories)
  }

  private fun loadSubcategories() {
    val subcategories = listOf("Subcategory 1", "Subcategory 2", "Subcategory 3")
    categoryAdapter.updateData(subcategories)
  }
}
