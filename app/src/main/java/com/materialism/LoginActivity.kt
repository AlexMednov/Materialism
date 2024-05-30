package com.materialism

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val loginButton = findViewById<Button>(R.id.login_button)
        val registerTextView = findViewById<TextView>(R.id.tv_register_here)

        loginButton.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(this, MainPageActivity::class.java)
                startActivity(intent)
            })

        registerTextView.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            })
    }

//    fun writeData() {
//        val database = FirebaseDatabase.getInstance().reference
//        val message = User()
//        database.child("User").push().setValue(message)
//    } Example of how to input data into
}
