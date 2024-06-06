package com.materialism

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.materialism.utils.DrawerUtils

class AddSubcategoryActivity : AppCompatActivity() {
  private lateinit var drawerLayout: DrawerLayout
  private lateinit var navView: NavigationView
  private var databaseManager = DatabaseManager(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_subcategory)
    databaseManager.open()

    drawerLayout = findViewById(R.id.drawer_layout)
    navView = findViewById(R.id.nav_view)

    DrawerUtils.setupDrawerContent(this, navView, drawerLayout)

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

    val icMenu = findViewById<ImageView>(R.id.ic_menu)
    icMenu.setOnClickListener { DrawerUtils.openDrawer(drawerLayout) }
  }

  private fun addSubcategoryView(subcategoryName: String) {
    //        val textView = TextView(this)
    //        textView.text = subcategoryName
    //        textView.tag = subcategoryName // Set tag to identify the view later
    //        val params =
    //            FlexboxLayout.LayoutParams(
    //                FlexboxLayout.LayoutParams.WRAP_CONTENT,
    // FlexboxLayout.LayoutParams.WRAP_CONTENT)
    //        params.setMargins(10, 10, 10, 10) // Add margins
    //        textView.layoutParams = params
    //
    //        flexboxLayout.addView(textView)
  }

  private fun loadSubcategories(): ArrayList<String> {
    //        val subcategoriesCursor = databaseManager.getAllSubcategories()
    //        val subcategoryArray = ArrayList<String>()
    //
    //        if (subcategoriesCursor.moveToFirst()) {
    //            do {
    //                val key =
    // subcategoriesCursor.getString(subcategoriesCursor.getColumnIndexOrThrow("name"))
    //                subcategoryArray.add(key)
    //            } while (subcategoriesCursor.moveToNext())
    //            subcategoriesCursor.close()
    //        }
    //
    //        return subcategoryArray
    return ArrayList()
  }

  private fun saveSubcategory(subcategoryName: String) {
    //        databaseManager.addSubcategory(subcategoryName, null, false)
  }

  private fun deleteSubcategory(subcategoryName: String) {
    //        val subcategoriesCursor = databaseManager.getAllSubcategories()
    //        if (subcategoriesCursor.moveToFirst()) {
    //            do {
    //                val key =
    // subcategoriesCursor.getString(subcategoriesCursor.getColumnIndexOrThrow("name"))
    //                if (key == subcategoryName) {
    //                    val subcategoryId =
    // subcategoriesCursor.getInt(subcategoriesCursor.getColumnIndexOrThrow("id"))
    //                    databaseManager.deleteSubcategory(subcategoryId)
    //                }
    //            } while (subcategoriesCursor.moveToNext())
    //            subcategoriesCursor.close()
    //        }
  }
}
