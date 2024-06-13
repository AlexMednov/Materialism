package com.materialism

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.materialism.utils.DrawerUtils

class ViewCategoriesActivity : ComponentActivity() {

  private lateinit var recyclerView: RecyclerView
  private lateinit var categoryAdapter: CategoryAdapter
  private lateinit var drawerLayout: DrawerLayout
  private lateinit var navView: NavigationView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_view_categories)

    drawerLayout = findViewById(R.id.drawer_layout)
    navView = findViewById(R.id.nav_view)

    DrawerUtils.setupDrawerContent(this, navView, drawerLayout)

    val spinnerCategoryType = findViewById<Spinner>(R.id.spinner_category_type)
    recyclerView = findViewById(R.id.recycler_view)
    val icMenu = findViewById<ImageView>(R.id.ic_menu)

    icMenu.setOnClickListener { DrawerUtils.openDrawer(drawerLayout) }

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
