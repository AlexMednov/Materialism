package com.materialism.friend

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.materialism.R
import com.materialism.adapter.RequestAdapter
import com.materialism.utils.DrawerUtils

class RequestItemActivity : ComponentActivity() {

  private lateinit var recyclerView: RecyclerView
  private lateinit var requestAdapter: RequestAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_request)

    val menuIcon: ImageButton = findViewById(R.id.ic_menu)
    DrawerUtils.setupPopupMenu(this, menuIcon)

    val backButton: ImageButton = findViewById(R.id.back_button)
    backButton.setOnClickListener { onBackPressed() }

    val spinnerRequestType = findViewById<Spinner>(R.id.spinner_request_type)
    recyclerView = findViewById(R.id.recycler_view)

    recyclerView.layoutManager = LinearLayoutManager(this)
    requestAdapter = RequestAdapter()
    recyclerView.adapter = requestAdapter

    val requestTypes = arrayOf("Incoming requests", "Your requests")
    val spinnerAdapter = ArrayAdapter(this, R.layout.custom_spinner_item, requestTypes)
    spinnerRequestType.adapter = spinnerAdapter

    spinnerRequestType.onItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
          override fun onItemSelected(
              parent: AdapterView<*>?,
              view: View?,
              position: Int,
              id: Long
          ) {
            when (position) {
              0 -> loadIncomingRequests()
              1 -> loadYourRequests()
            }
          }

          override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
  }

  private fun loadIncomingRequests() {
    // Create logic for API calls via an HTTP client like Retrofit
  }

  private fun loadYourRequests() {
    // Create logic for API calls via an HTTP client like Retrofit
  }
}
