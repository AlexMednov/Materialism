package com.materialism

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.materialism.ui.theme.MaterialismTheme

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

        backButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
        })
    }
}