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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        flexboxLayout = findViewById(R.id.flexboxLayout)

        // Load and display existing categories
        loadCategories()?.forEach { addCategoryView(it.name) }

        val addCategoryButton = findViewById<Button>(R.id.addCategoryButton)
        addCategoryButton.setOnClickListener {
            val categoryNameEditText = findViewById<EditText>(R.id.categoryName)
            val categoryName = categoryNameEditText.text.toString()
            if (categoryName.isNotBlank()) {
                val category = Category(categoryName)
                addCategoryView(categoryName)
                saveCategory(category)
                categoryNameEditText.text.clear()
            }
        }

        val deleteCategoryButton = findViewById<Button>(R.id.deleteCategoryButton)
        deleteCategoryButton.setOnClickListener {
            val categoryNameEditText = findViewById<EditText>(R.id.categoryName)
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
        textView.tag = categoryName  // Set tag to identify the view later
        val params = FlexboxLayout.LayoutParams(
            FlexboxLayout.LayoutParams.WRAP_CONTENT,
            FlexboxLayout.LayoutParams.WRAP_CONTENT
        )
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

    private fun saveCategory(category: Category) {
        val categories = loadCategories().toMutableList()
        categories.add(category)
        saveCategories(categories)
    }

    private fun saveCategories(categories: List<Category>) {
        val categoriesJson = gson.toJson(categories)
        applicationContext.openFileOutput(categoriesFileName, MODE_PRIVATE).use {
            it.write(categoriesJson.toByteArray())
        }
    }

    private fun loadCategories(): List<Category> {
        val file = File(applicationContext.filesDir, categoriesFileName)
        if (!file.exists()) {
            return emptyList()
        }
        return applicationContext.openFileInput(categoriesFileName).bufferedReader().use { reader ->
            val type = object : TypeToken<List<Category>>() {}.type
            val data = reader.readText()
            if (data.trim().startsWith("[")) {
                gson.fromJson(data, type)
            } else {
                listOf(gson.fromJson(data, Category::class.java))
            }
        }
    }

    private fun deleteCategory(categoryName: String) {
        val categories = loadCategories().toMutableList()
        val iterator = categories.iterator()
        while (iterator.hasNext()) {
            val category = iterator.next()
            if (category.name == categoryName) {
                iterator.remove()
                break
            }
        }
        saveCategories(categories)
    }
}
