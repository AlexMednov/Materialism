package com.materialism

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.materialism.databinding.ActivityViewItemsBinding
import com.materialism.sampledata.Item

class ViewItemsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewItemsBinding
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup back button
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        setupSortSpinner()
        setupRecyclerView()
    }

    private fun setupSortSpinner() {
        val sortOptions = arrayOf("Sort by Category", "Sort by Name Ascending", "Sort by Name Descending")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sortOptions)
        binding.sortSpinner.adapter = adapter

        binding.sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (position) {
                    0 -> itemAdapter.sortByCategory()
                    1 -> itemAdapter.sortByNameAsc()
                    2 -> itemAdapter.sortByNameDesc()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupRecyclerView() {
        itemAdapter = ItemAdapter(getItems(), true) // Pass true to show edit button
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ViewItemsActivity)
            adapter = itemAdapter
        }
    }

    private fun getItems(): List<Item> {
        return listOf(
            Item("Apple", "Fruit", "Food", "Location A", "2023-05-21"),
            Item("Banana", "Fruit", "Food", "Location B", "2023-05-22"),
            Item("Porsche 911", "Vehicle", "Car", "Location C", "2023-05-23"),
            Item("Star Wars", "lego set", "Lego", "Location D", "2023-05-24")
        )
    }
}
