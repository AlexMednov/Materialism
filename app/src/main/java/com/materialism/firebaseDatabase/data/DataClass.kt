package com.materialism.firebaseDatabase.data

data class Quest(
    val id: Int = 0,
    val categoryId: Int = 1,
    val weight: Int = 2,
    val type: String = "Daily"
) {
  constructor() : this(id = 0, categoryId = 1, weight = 2, type = "Daily")
}

data class QuestItem(val id: Int = 0, val name: String = "Default name", val categoryId: Int = 0) {
  constructor() : this(id = 0, name = "Default name", categoryId = 0)
}

data class User(
    val id: Int = 0,
    var name: String = "User Userovich",
    var email: String? = null,
    val hashedPassword: String? = null,
    val isRegistered: Boolean = false,
    val score: Int = 0,
    val karma: Int = 0,
    var location: String? = null
) {
  constructor() :
      this(
          id = 0,
          name = "User Userovich",
          email = null,
          hashedPassword = null,
          isRegistered = false,
          score = 0,
          karma = 0,
          location = null)
}

data class Category(
    val description: String? = null,
    val id: Int = 0,
    val isDefault: Boolean = false,
    val name: String = "Default category"
) {
  constructor() : this(description = null, id = 0, isDefault = false, name = "Default category")
}

data class SubCategory(
    val id: Int = 0,
    val name: String = "Default name",
    val description: String? = null,
    val categoryId: Int = 0
) {
  constructor() : this(id = 0, name = "Default name", description = null, categoryId = 0)
}

data class Item(
    val id: Int = 0,
    val name: String = "Default name",
    val imageURI: String = "defaultImage.png",
    val description: String? = null,
    val location: String? = null,
    val isPublic: Boolean = false,
    val isLoaned: Boolean = false,
    val dateAdded: String = "",
    val dateModified: String = "",
    val userId: Int = 0,
    val categoryId: Int = 0,
    val subcategoryId: Int = 0
) {
  constructor() :
      this(
          id = 0,
          name = "Default name",
          imageURI = "defaultImage.png",
          description = null,
          location = null,
          isPublic = false,
          isLoaned = false,
          dateAdded = "",
          dateModified = "",
          userId = 0,
          categoryId = 0,
          subcategoryId = 0)
}

data class Friend(val userId1: Int = 0, val userId2: Int = 1) {
  constructor() : this(userId1 = 0, userId2 = 1)
}
