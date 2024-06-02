package com.materialism.firebaseDatabase.data

import java.util.Date

data class QuestItem(val id: Int = 0, val name: String = "Default name", val categoryId: Int = 0)

data class Quest(val id: Int = 0, val type: String = "Default quest", val weight: Int = 0, val categoryId: Int = 0)

data class User(val id: Int = 0, val name: String = "User Userovich", val email: String? = null, val hashedPassword: String? = null,val isRegistered: Boolean = false, val score: Int = 0, val karma: Int = 0, val location: String? = null)

data class Category(val id: Int = 0, val name: String = "Default category", val description: String? = null, val isDefault: Boolean = false)

data class SubCategory(val id: Int = 0, val name: String = "Default name", val description: String? = null, val categoryId: Int = 0)

data class Item(val id: Int = 0, val name: String = "Default name", val imageURI: String = "defaultImage.png", val description: String? = null, val location: String? = null, val isPublic: Boolean = false, val isLoaned: Boolean = false, val dateAdded: Date, val dateModified: Date? = null, val userId: Int = 0, val categoryId: Int = 0, val subcategoryId: Int = 0)