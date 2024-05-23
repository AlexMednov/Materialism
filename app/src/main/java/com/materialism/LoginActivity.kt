package com.materialism

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity

class LoginActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.login_activity)

    val loginButton = findViewById<Button>(R.id.loginButton)

    loginButton.setOnClickListener(
        View.OnClickListener {
          val intent = Intent(this, MainPageActivity::class.java)

          startActivity(intent)
        })
  }
}
