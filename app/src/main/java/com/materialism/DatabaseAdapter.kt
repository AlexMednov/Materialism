package com.materialism

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import android.database.Cursor
import android.util.Log
import com.materialism.firebaseDatabase.data.Category
import com.materialism.firebaseDatabase.data.SubCategory
import com.materialism.firebaseDatabase.data.Quest
import com.materialism.firebaseDatabase.data.QuestItem
import com.materialism.firebaseDatabase.data.Item


class DatabaseAdapter(val databaseManager: DatabaseManager) {

    private val firebaseDatabase = FirebaseDatabase.getInstance().reference

    // Get categories from Firebase and write them to SQLite if they do not already exist
    fun syncCategories() {
        Log.d("SyncCategories", "Starting synchronization")

        firebaseDatabase.child("Category").get().addOnSuccessListener { dataSnapshot ->
            dataSnapshot.children.forEach { categorySnapshot ->
                try {
                    val category = categorySnapshot.getValue(Category::class.java)
                    if (category != null) {
                        val localCategoryCursor = databaseManager.getCategory(category.id)
                        if (!localCategoryCursor.moveToFirst()) {
                            databaseManager.addCategory(category.name, category.description, category.isDefault)
                            Log.d("SyncCategories", "Category added: ${category.name}")
                        } else {
                            Log.d("SyncCategories", "Category already exists: ${category.name}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SyncCategories", "Error processing category: ${e.message}")
                }
            }
        }.addOnFailureListener { exception ->
            Log.e("SyncCategories", "Error retrieving categories from Firebase: ${exception.message}")
        }
    }


    // Get subcategories from Firebase and write them to SQLite if they do not already exist
    fun syncSubCategories() {
        Log.d("SyncSubCategories", "Starting synchronization")

        firebaseDatabase.child("SubCategory").get().addOnSuccessListener { dataSnapshot ->
        dataSnapshot.children.forEach { subcategorySnapshot ->
            try {
                val subcategory = subcategorySnapshot.getValue(SubCategory::class.java)
                if (subcategory != null) {
                    val localSubCategoryCursor = databaseManager.getSubcategory(subcategory.id)
                    if (!localSubCategoryCursor.moveToFirst()) {
                        databaseManager.addSubcategory(subcategory.name, subcategory.categoryId)
                            Log.d("SyncSubCategories", "SubCategory added: ${subcategory.name}")
                        } else {
                            Log.d("SyncSubCategories", "SubCategory already exists: ${subcategory.name}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SyncCategories", "Error processing category: ${e.message}")
                }
            }
        }.addOnFailureListener { exception ->
            Log.e("SyncSubCategories", "Error retrieving subcategories from Firebase: ${exception.message}")
        }
    }

    // Add item to Firebase if isPublic is true
    fun addItem(item: Item, userId: String) {
        if (item.isPublic) {
            firebaseDatabase.child("Users").child(userId).child("Items").push().setValue(item)
        }
    }

    // Update item in Firebase if isPublic is true
    fun updateItem(item: Item, userId: String) {
        if (item.isPublic) {
            firebaseDatabase.child("Users").child(userId).child("Items").child(item.id.toString()).setValue(item)
        }
    }

    // Delete item from Firebase
    fun deleteItem(itemId: Int, userId: String) {
        firebaseDatabase.child("Users").child(userId).child("Items").child(itemId.toString()).removeValue()
    }

    // Get quests from Firebase and write them to SQLite if they do not already exist
    fun syncQuests() {
        Log.d("SyncQuests", "Starting synchronization")

        firebaseDatabase.child("Quests").get().addOnSuccessListener { dataSnapshot ->
        dataSnapshot.children.forEach { questSnapshot ->
            try {
                val quest = questSnapshot.getValue(Quest::class.java)
                if (quest != null) {
                    val localQuestCursor = databaseManager.getQuest(quest.id)
                    if (!localQuestCursor.moveToFirst()) {
                        databaseManager.addQuest(quest.type, quest.weight, quest.categoryId)
                        Log.d("SyncQuests", "Quest added: ${quest.type}")
                    } else {
                        Log.d("SyncQuests", "Quest already exists: ${quest.type}")
                    }
                }
            } catch (e: Exception) {
                Log.e("SyncQuests", "Error processing category: ${e.message}")
            }
        }
        }.addOnFailureListener { exception ->
            // Handle any errors
            Log.e("SyncQuests", "Error retrieving quests from Firebase: ${exception.message}")
        }
    }

    // Get quest items from Firebase and write them to SQLite if they do not already exist
    fun syncQuestItems() {
        firebaseDatabase.child("QuestItems").get().addOnSuccessListener { dataSnapshot ->
            dataSnapshot.children.forEach { questItemSnapshot ->
                try {
                    val questItem = questItemSnapshot.getValue(QuestItem::class.java)
                    if (questItem != null) {
                        val localQuestItemCursor = databaseManager.getQuestItem(questItem.id)
                        if (!localQuestItemCursor.moveToFirst()) {
                            databaseManager.addQuestItem(questItem.name, questItem.categoryId)
                            Log.d("SyncQuestItems", "Category added: ${questItem.name}")
                        } else {
                            Log.d("SyncQuestItems", "QuestItem already exists: ${questItem.name}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SyncQuestItems", "Error processing quest item: ${e.message}")
                }
            }
        }.addOnFailureListener { exception ->
            // Handle any errors
            Log.e("SyncQuestItems", "Error retrieving quest items from Firebase: ${exception.message}")
        }
    }
}

