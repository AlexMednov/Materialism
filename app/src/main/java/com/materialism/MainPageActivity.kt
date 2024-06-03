package com.materialism

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity

class MainPageActivity : ComponentActivity() {

    private val databaseAdapter = DatabaseAdapter(DatabaseManager(this))
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_page_activity)

    databaseAdapter.databaseManager.open()
    databaseAdapter.syncCategories()
    databaseAdapter.syncSubCategories()
    databaseAdapter.syncQuests()
    databaseAdapter.syncQuestItems()

    val categoryButton = findViewById<Button>(R.id.category_button)

    categoryButton.setOnClickListener(
        View.OnClickListener {
          val intent = Intent(this, AddCategoryActivity::class.java)

          startActivity(intent)
        })

    val accessCameraButton = findViewById<Button>(R.id.access_camera)

    accessCameraButton.setOnClickListener(
        View.OnClickListener {
          val intent = Intent(applicationContext, CameraActivity::class.java)

          startActivity(intent)
        })

    val backButton = findViewById<Button>(R.id.back)

    val addItemButton = findViewById<Button>(R.id.add_items)
    addItemButton.setOnClickListener(
        View.OnClickListener {
          val intent = Intent(this, AddItemActivity::class.java)

          startActivity(intent)
        })

    backButton.setOnClickListener(
        View.OnClickListener {
          val intent = Intent(this, LoginActivity::class.java)

          startActivity(intent)
        })

    val designButton: Button = findViewById(R.id.design_button)
    designButton.setOnClickListener {
      val intent = Intent(this, DesignMainPageActivity::class.java)
      startActivity(intent)
    }
  }
}
