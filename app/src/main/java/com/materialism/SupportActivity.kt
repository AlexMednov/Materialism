package com.materialism

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.materialism.utils.DrawerUtils

class SupportActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_support)

        val menuIcon: ImageButton = findViewById(R.id.ic_menu)
        DrawerUtils.setupPopupMenu(this, menuIcon)
    }
}
