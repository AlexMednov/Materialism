package com.materialism

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import android.database.Cursor
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.materialism.firebaseDatabase.data.*


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

    fun getFriendsUserIds(loggedInUserId: String, callback: (List<String>) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Friend")
        databaseReference.child(loggedInUserId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val friendsUserIds = mutableListOf<String>()
                    for (snapshot in dataSnapshot.children) {
                        val friendId = snapshot.getValue(String::class.java)
                        if (friendId != null && friendId != loggedInUserId) {
                            friendsUserIds.add(friendId)
                        }
                    }
                    callback(friendsUserIds)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
    }

    fun getUsersInformation(userIds: List<String>, callback: (List<User>) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val users = mutableListOf<User>()

        userIds.forEach { userId ->
            databaseReference.child(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val user = dataSnapshot.getValue(User::class.java)
                        if (user != null) {
                            users.add(user)
                        }

                        if (users.size == userIds.size) {
                            callback(users)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle error
                    }
                })
        }
    }

    fun getItemsForUsers(userIds: List<String>, callback: (List<Item>) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Items")
        val items = mutableListOf<Item>()

        userIds.forEach { userId ->
            databaseReference.orderByChild("userId").equalTo(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot in dataSnapshot.children) {
                            val item = snapshot.getValue(Item::class.java)
                            if (item != null) {
                                items.add(item)
                            }
                        }

                        if (userIds.indexOf(userId) == userIds.size - 1) {
                            callback(items)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle error
                    }
                })
        }
    }

    fun getSingleUserInformation(friendUserId: String, callback: (User?) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.child(friendUserId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(User::class.java)
                    callback(user)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    callback(null)
                }
            })
    }

    fun getItemsForSingleUser(friendUserId: String, callback: (List<Item>) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Items")
        databaseReference.orderByChild("userId").equalTo(friendUserId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val items = mutableListOf<Item>()
                    for (snapshot in dataSnapshot.children) {
                        val item = snapshot.getValue(Item::class.java)
                        if (item != null) {
                            items.add(item)
                        }
                    }
                    callback(items)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    callback(emptyList())
                }
            })
    }

    fun fetchFriendInformationAndItems(friendUserId: String) {
        getSingleUserInformation(friendUserId) { user ->
            if (user != null) {
                // User information retrieved.
                println("User Info: $user")

                // Now retrieve the items for this user.
                getItemsForSingleUser(friendUserId) { items ->
                    // Process or display items associated with the friend.
                    println("Items: $items")
                }
            } else {
                // Handle the case where user information retrieval failed.
                println("Failed to retrieve user information.")
            }
        }
    }
}

