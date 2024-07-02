package com.materialism.category

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.materialism.MainPageActivity
import com.materialism.R
import com.materialism.database.localDatabase.DatabaseManager
import com.materialism.utils.DrawerUtils

class AddSubcategoryActivity : AppCompatActivity() {
  private var databaseManager = DatabaseManager(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_subcategory)
    databaseManager.open()

    val menuIcon: ImageButton = findViewById(R.id.ic_menu)
    DrawerUtils.setupPopupMenu(this, menuIcon)

    val backButton: ImageButton = findViewById(R.id.back_button)
    backButton.setOnClickListener { onBackPressed() }

    val addSubcategoryButton = findViewById<Button>(R.id.add_subcategory_button)
    addSubcategoryButton.setOnClickListener {
      val subcategoryNameEditText = findViewById<EditText>(R.id.subcategory_name)
      val subcategoryName = subcategoryNameEditText.text.toString()
      if (subcategoryName.isNotBlank()) {
        addSubcategoryView(subcategoryName)
        saveSubcategory(subcategoryName)
        subcategoryNameEditText.text.clear()

        //Creating alertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Subcategory addition")
        builder.setMessage("Your subcategory has been successfully added")
        builder.setPositiveButton("Ok"){dialog, which ->//Setting ok button
          // send to previous page upon acknowledgment
          val intent = Intent(this, MainPageActivity::class.java)
          startActivity(intent)
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()//showing alertDialog
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