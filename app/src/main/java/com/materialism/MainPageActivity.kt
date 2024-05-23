package com.materialism

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity

class MainPageActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_page_activity)

        val categoryButton = findViewById<Button>(R.id.categoryButton)

        categoryButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AddCategoryActivity::class.java)

            startActivity(intent)
        })

        val backButton = findViewById<Button>(R.id.back)

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

      val designButton: Button = findViewById(R.id.DesignButton)
      designButton.setOnClickListener {
          val intent = Intent(this, DesignMainPageActivity::class.java)
          startActivity(intent)
      }
  }
}
