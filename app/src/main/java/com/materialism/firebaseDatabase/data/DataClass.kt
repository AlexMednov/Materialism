package com.materialism.firebaseDatabase.data

data class User(
    val id: String = "",
    val name: String = "",
    val emailAddress: String? = null,
    val isRegistered: Boolean = false,
    val score: Int = 0,
    val karma: Int = 0
)

data class Item(
    val id: String = "",
    val name: String = "",
    val imageURI: String = "",
    val description: String? = null,
    val location: String? = null,
    val isPublic: Boolean = false,
    val isLoaned: Boolean = false,
    val dateTimeAdded: String = "",
    val dateTimeModified: String = "",
    val userId: String = "",
    val categoryId: String = "",
    val subcategoryId: String? = null
)

data class Subcategory(
    val id: String = "",
    val name: String = "",
    val description: String? = null
)

data class Category(
    val id: String = "",
    val name: String = "",
    val description: String? = null,
    val isDefault: Boolean = false,
    val subcategories: Map<String, Subcategory> = emptyMap()
)

data class Quest(
    val id: String = "",
    val type: Int = 0,
    val weight: Int = 0,
    val categoryId: String = ""
)

data class QuestItem(
    val id: String = "",
    val name: String = "",
    val categoryId: String = ""
)

data class FirebaseData(
    val users: Map<String, User> = emptyMap(),
    val items: Map<String, Item> = emptyMap(),
    val categories: Map<String, Category> = emptyMap(),
    val quests: Map<String, Quest> = emptyMap(),
    val questItems: Map<String, QuestItem> = emptyMap()
)

//Need to go through both json and dataclass more precisely, for they appear to be a bit goofy

