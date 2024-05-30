package com.materialism

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.flexbox.FlexboxLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

data class Category(val name: String)

class AddCategoryActivity : AppCompatActivity() {
  private lateinit var flexboxLayout: FlexboxLayout
  private val gson = Gson()
  private val categoriesFileName = "categories.json"
  private var databaseManager = DatabaseManager(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_category)
    databaseManager.open()

    flexboxLayout = findViewById(R.id.flexbox_layout)

    // Load and display existing categories
    loadCategories().forEach { addCategoryView(it) }

    val addCategoryButton = findViewById<Button>(R.id.add_category_button)
    addCategoryButton.setOnClickListener {
      val categoryNameEditText = findViewById<EditText>(R.id.category_name)
      val categoryName = categoryNameEditText.text.toString()
      if (categoryName.isNotBlank()) {
        addCategoryView(categoryName)
        saveCategory(categoryName)
        categoryNameEditText.text.clear()
      }
    }

    val deleteCategoryButton = findViewById<Button>(R.id.delete_category_button)
    deleteCategoryButton.setOnClickListener {
      val categoryNameEditText = findViewById<EditText>(R.id.category_name)
      val categoryName = categoryNameEditText.text.toString()
      if (categoryName.isNotBlank()) {
        deleteCategory(categoryName)
        removeCategoryView(categoryName)
        categoryNameEditText.text.clear()
      }
    }
  }

  private fun addCategoryView(categoryName: String) {
    val textView = TextView(this)
    textView.text = categoryName
    textView.tag = categoryName // Set tag to identify the view later
    val params =
        FlexboxLayout.LayoutParams(
            FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT)
    params.setMargins(10, 10, 10, 10) // Add margins
    textView.layoutParams = params

    flexboxLayout.addView(textView)
  }

  private fun removeCategoryView(categoryName: String) {
    for (i in 0 until flexboxLayout.childCount) {
      val view = flexboxLayout.getChildAt(i)
      if (view is TextView && view.tag == categoryName) {
        flexboxLayout.removeView(view)
        break
      }
    }
  }

  private fun loadCategories(): ArrayList<String> {
    val categoriesCursor = databaseManager.getAllCategories()
    val categoryArray = ArrayList<String>()

    if (categoriesCursor.moveToFirst()) {
      do {
        val key = categoriesCursor.getString(categoriesCursor.getColumnIndexOrThrow("name"))
        categoryArray.add(key)
      } while (categoriesCursor.moveToNext())
      categoriesCursor.close()
    }

    return categoryArray
  }

  private fun saveCategory(categoryName: String) {
    databaseManager.addCategory(categoryName, null, false)
  }

  private fun deleteCategory(categoryName: String) {
    databaseManager.deleteCategory(0)
  }

}
