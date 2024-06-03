package com.materialism

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import android.database.Cursor
import com.materialism.firebaseDatabase.data.Category
import com.materialism.firebaseDatabase.data.SubCategory
import com.materialism.firebaseDatabase.data.Quest
import com.materialism.firebaseDatabase.data.QuestItem
import com.materialism.firebaseDatabase.data.Item


class DatabaseAdapter(private val databaseManager: DatabaseManager) {

    private val firebaseDatabase = FirebaseDatabase.getInstance().reference

    // Get categories from Firebase and write them to SQLite if they do not already exist
    fun syncCategories() {
        firebaseDatabase.child("Categories").get().addOnSuccessListener { dataSnapshot ->
            for (categorySnapshot in dataSnapshot.children) {
                val category = categorySnapshot.getValue(Category::class.java)
                if (category != null) {
                    val localCategoryCursor = databaseManager.getCategory(category.id)
                    if (!localCategoryCursor.moveToFirst()) {
                        databaseManager.addCategory(category.name, category.description, category.isDefault)
                    }
                }
            }
        }.addOnFailureListener {
            // Handle any errors
        }
    }

    // Get subcategories from Firebase and write them to SQLite if they do not already exist
    fun syncSubcategories() {
        firebaseDatabase.child("Subcategories").get().addOnSuccessListener { dataSnapshot ->
            for (subcategorySnapshot in dataSnapshot.children) {
                val subcategory = subcategorySnapshot.getValue(SubCategory::class.java)
                if (subcategory != null) {
                    val localSubcategoryCursor = databaseManager.getSubcategory(subcategory.id)
                    if (!localSubcategoryCursor.moveToFirst()) {
                        databaseManager.addSubcategory(subcategory.name, subcategory.categoryId)
                    }
                }
            }
        }.addOnFailureListener {
            // Handle any errors
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
        firebaseDatabase.child("Quests").get().addOnSuccessListener { dataSnapshot ->
            for (questSnapshot in dataSnapshot.children) {
                val quest = questSnapshot.getValue(Quest::class.java)
                if (quest != null) {
                    val localQuestCursor = databaseManager.getQuest(quest.id)
                    if (!localQuestCursor.moveToFirst()) {
                        databaseManager.addQuest(quest.type, quest.weight, quest.categoryId)
                    }
                }
            }
        }.addOnFailureListener {
            // Handle any errors
        }
    }

    // Get quest items from Firebase and write them to SQLite if they do not already exist
    fun syncQuestItems() {
        firebaseDatabase.child("QuestItems").get().addOnSuccessListener { dataSnapshot ->
            for (questItemSnapshot in dataSnapshot.children) {
                val questItem = questItemSnapshot.getValue(QuestItem::class.java)
                if (questItem != null) {
                    val localQuestItemCursor = databaseManager.getQuestItem(questItem.id)
                    if (!localQuestItemCursor.moveToFirst()) {
                        databaseManager.addQuestItem(questItem.name, questItem.categoryId)
                    }
                }
            }
        }.addOnFailureListener {
            // Handle any errors
        }
    }
}

