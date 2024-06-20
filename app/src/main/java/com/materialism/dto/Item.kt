package com.materialism.dto

// What is this used for? Item in DataClass.kt already has a constructor
data class Item(
    val name: String,
    val description: String,
    val imageUri: String,
    val category: String,
    val location: String,
    val date: String,
    val id: Int = 0
)
