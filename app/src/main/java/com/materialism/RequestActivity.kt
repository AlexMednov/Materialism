package com.materialism

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class RequestActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var requestAdapter: RequestAdapter
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        DrawerUtils.setupDrawerContent(this, navView, drawerLayout)

        val spinnerRequestType = findViewById<Spinner>(R.id.spinner_request_type)
        recyclerView = findViewById(R.id.recycler_view)
        val icMenu = findViewById<ImageView>(R.id.ic_menu)

        icMenu.setOnClickListener { DrawerUtils.openDrawer(drawerLayout) }

        recyclerView.layoutManager = LinearLayoutManager(this)
        requestAdapter = RequestAdapter()
        recyclerView.adapter = requestAdapter

        val requestTypes = arrayOf("Incoming requests", "Your requests")
        val spinnerAdapter = ArrayAdapter(this, R.layout.custom_spinner_item, requestTypes)
        spinnerRequestType.adapter = spinnerAdapter

        spinnerRequestType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> loadIncomingRequests()
                    1 -> loadYourRequests()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun loadIncomingRequests() {
        val incomingRequests = listOf(
            Request("Item name", "Item description", "Category", "Location", "Date"),
            Request("Item name", "Item description", "Category", "Location", "Date"),
            Request("Item name", "Item description", "Category", "Location", "Date")
        )
        requestAdapter.updateData(incomingRequests, RequestAdapter.ViewType.INCOMING)
    }

    private fun loadYourRequests() {
        val yourRequests = listOf(
            Request("Item name", "Item description", "Category", "Location", "Date", "Name Surname", "Pending"),
            Request("Item name", "Item description", "Category", "Location", "Date", "Name Surname", "Confirmed")
        )
        requestAdapter.updateData(yourRequests, RequestAdapter.ViewType.YOUR)
    }
}
