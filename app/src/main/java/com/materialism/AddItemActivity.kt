package com.materialism

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.materialism.databinding.ActivityAddItemBinding

class AddItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        val categories = resources.getStringArray(R.array.categories_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter

        binding.selectPictureButton.setOnClickListener {
        }

        binding.addItemButton.setOnClickListener {
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

// TODO: Item private should be No by deffault
