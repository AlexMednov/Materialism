package com.materialism.sidebar

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatDelegate
import com.materialism.R
import com.materialism.utils.DrawerUtils

class SettingsActivity : ComponentActivity() {

  private var isDarkTheme = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)

    val buttonToggleTheme = findViewById<Button>(R.id.button_toggle_theme)
    buttonToggleTheme.setOnClickListener {
      isDarkTheme = !isDarkTheme
      if (isDarkTheme) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
      } else {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
      }
    }

    val menuIcon: ImageButton = findViewById(R.id.ic_menu)
    DrawerUtils.setupPopupMenu(this, menuIcon)

    val backButton: ImageButton = findViewById(R.id.back_button)
    backButton.setOnClickListener { onBackPressed() }
  }
}
