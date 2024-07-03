package com.materialism.category

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.flexbox.FlexboxLayout
import com.materialism.MainPageActivity
import com.materialism.R
import com.materialism.database.localDatabase.DatabaseManager
import com.materialism.utils.DrawerUtils

class AddCategoryActivity : AppCompatActivity() {

  private lateinit var flexboxLayout: FlexboxLayout
  private var databaseManager = DatabaseManager(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_category)
    databaseManager.open()

    flexboxLayout = findViewById(R.id.flexbox_layout)

    val menuIcon: ImageButton = findViewById(R.id.ic_menu)
    DrawerUtils.setupPopupMenu(this, menuIcon)

    val backButton: ImageButton = findViewById(R.id.back_button)
    backButton.setOnClickListener { onBackPressed() }

    val addCategoryButton = findViewById<Button>(R.id.add_category_button)
    addCategoryButton.setOnClickListener {
      val categoryNameEditText = findViewById<EditText>(R.id.category_name)
      val categoryName = categoryNameEditText.text.toString()
      if (categoryName.isNotBlank()) {
        addCategoryView(categoryName)
        saveCategory(categoryName)
        categoryNameEditText.text.clear()

        // Creating alert dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Category addition")
        builder.setMessage("Your category has been successfully added")
        builder.setPositiveButton("Ok") { dialog, which -> // Setting ok button
          // send to previous page upon acknowledgment
          val intent = Intent(this, MainPageActivity::class.java)
          startActivity(intent)
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show() // showing alertDialog
      }
    }

    val viewCategoriesButton = findViewById<Button>(R.id.view_categories_button)
    viewCategoriesButton.setOnClickListener {
      val intent = Intent(this, ViewCategoriesActivity::class.java)
      startActivity(intent)
    }

    // Load and display existing categories
    loadCategories().forEach { addCategoryView(it) }
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
    val categoriesCursor = databaseManager.getAllCategories()
    if (categoriesCursor.moveToFirst()) {
      do {
        val key = categoriesCursor.getString(categoriesCursor.getColumnIndexOrThrow("name"))
        if (key == categoryName) {
          val categoryId = categoriesCursor.getInt(categoriesCursor.getColumnIndexOrThrow("id"))
          databaseManager.deleteCategory(categoryId)
        }
      } while (categoriesCursor.moveToNext())
      categoriesCursor.close()
    }
  }
}
