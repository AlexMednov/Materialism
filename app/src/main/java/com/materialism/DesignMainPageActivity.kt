package com.materialism

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.materialism.databinding.DesignMainPageActivityBinding

class DesignMainPageActivity : AppCompatActivity() {

    private lateinit var binding: DesignMainPageActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DesignMainPageActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.level.text = "Level: 1"
        binding.progressBar.progress = 10
        binding.progressBar.max = 20
        binding.exp.text = "Exp: 10/20"

        val items = listOf(
            Item("Item 1", "Description 1"),
            Item("Item 2", "Description 2"),
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = ItemAdapter(items)

        binding.fab.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }
    }
}