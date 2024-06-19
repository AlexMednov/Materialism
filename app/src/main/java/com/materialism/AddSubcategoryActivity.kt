package com.materialism

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.materialism.utils.DrawerUtils

class AddSubcategoryActivity : AppCompatActivity() {
  private var databaseManager = DatabaseManager(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_subcategory)
    databaseManager.open()

    val menuIcon: ImageButton = findViewById(R.id.ic_menu)
    DrawerUtils.setupPopupMenu(this, menuIcon)

    val addSubcategoryButton = findViewById<Button>(R.id.add_subcategory_button)
    addSubcategoryButton.setOnClickListener {
      val subcategoryNameEditText = findViewById<EditText>(R.id.subcategory_name)
      val subcategoryName = subcategoryNameEditText.text.toString()
      if (subcategoryName.isNotBlank()) {
        addSubcategoryView(subcategoryName)
        saveSubcategory(subcategoryName)
        subcategoryNameEditText.text.clear()
      }
    }

    val viewCategoriesButton = findViewById<Button>(R.id.view_categories_button)
    viewCategoriesButton.setOnClickListener {
      val intent = Intent(this, ViewCategoriesActivity::class.java)
      startActivity(intent)
    }
  }

  private fun addSubcategoryView(subcategoryName: String) {
    // Implementation here
  }

  private fun loadSubcategories(): ArrayList<String> {
    // Implementation here
    return ArrayList()
  }

  private fun saveSubcategory(subcategoryName: String) {
    // Implementation here
  }

  private fun deleteSubcategory(subcategoryName: String) {
    // Implementation here
  }
}
