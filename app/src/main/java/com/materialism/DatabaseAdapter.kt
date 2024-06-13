package com.materialism

import com.google.firebase.database.FirebaseDatabase
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
                            databaseManager.addCategory(
                                category.name,
                                category.description,
                                category.isDefault
                            )
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
            Log.e(
                "SyncCategories",
                "Error retrieving categories from Firebase: ${exception.message}"
            )
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
                            Log.d(
                                "SyncSubCategories",
                                "SubCategory already exists: ${subcategory.name}"
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SyncCategories", "Error processing category: ${e.message}")
                }
            }
        }.addOnFailureListener { exception ->
            Log.e(
                "SyncSubCategories",
                "Error retrieving subcategories from Firebase: ${exception.message}"
            )
        }
    }

    // Add item to Firebase if isPublic is true
    fun addItem(item: Item, userId: Int) {
        if (item.isPublic) {
            firebaseDatabase.child("Users").child(userId.toString()).child("Items").push()
                .setValue(item)
        }
    }

    // Update item in Firebase if isPublic is true
    fun updateItem(item: Item, userId: Int) {
        if (item.isPublic) {
            firebaseDatabase.child("Users").child(userId.toString()).child("Items")
                .child(item.id.toString()).setValue(item)
        }
    }

    // Delete item from Firebase
    fun deleteItem(itemId: Int, userId: Int) {
        firebaseDatabase.child("Users").child(userId.toString()).child("Items")
            .child(itemId.toString()).removeValue()
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
            Log.e(
                "SyncQuestItems",
                "Error retrieving quest items from Firebase: ${exception.message}"
            )
        }
    }

    //passes
    fun getFriendsUserIds(loggedInUserId: Int, callback: (List<Int>) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Friend")
        databaseReference.child(loggedInUserId.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val friendsUserIds = mutableListOf<Int>()
                    Log.d("MainActivity", "DataSnapshot Friends: ${dataSnapshot.value}")

                    for (snapshot in dataSnapshot.children) {
                        val friendId = snapshot.getValue(Int::class.java)
                        Log.d("MainActivity", "Friend ID: $friendId")

                        if (friendId != null && friendId != loggedInUserId) {
                            friendsUserIds.add(friendId)
                        }
                    }
                    Log.d("MainActivity", "Final Friends User IDs: $friendsUserIds")
                    callback(friendsUserIds)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    Log.e("MainActivity", "Database error: ${databaseError.message}")
                }
            })
    }

    //passes, however all users are retrieved before filtering
    fun getUsersInformation(userIds: List<Int>, callback: (List<User>) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("User")
        val users = mutableListOf<User>()
        val userIdSet = userIds.toSet() // Use a set for faster lookups

        Log.d("MainActivity", "Fetching Users for IDs: $userIds")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)

                    if (user != null && userIdSet.contains(user.id)) {
                        users.add(user)
                    }
                }
                callback(users) // Call the callback with the retrieved users
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    //passes
    fun getItemsForUsers(userIds: List<Int>, callback: (List<Item>) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Item")
        val items = mutableListOf<Item>()

        userIds.forEach { userId ->
            databaseReference.orderByChild("userId").equalTo(userId.toString())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot in dataSnapshot.children) {
                            val item = snapshot.getValue(Item::class.java)
                            if (item != null) {
                                items.add(item)
                            }
                        }
                        // Checking when all queries are done.
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

    //passes
    fun getSingleUserInformation(userId: Int, callback: (User?) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("User")

        databaseReference.orderByChild("id").equalTo(userId.toDouble())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    Log.e("MainActivity", "Database error: ${dataSnapshot}")
                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.children) {
                            val user = snapshot.getValue(User::class.java)
                            callback(user)
                            return
                        }
                    } else {
                        callback(null)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    Log.e("MainActivity", "Database error: ${databaseError.message}")
                    callback(null)
                }
            })
    }

    //passes, but crashes due to there being no No-Argument Constructors for data classes
    fun getItemsForSingleUser(userId: Int, callback: (List<Item>) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Item")
        val items = mutableListOf<Item>()

        databaseReference.orderByChild("userId")
            .equalTo(userId.toDouble())  // Assuming userId is stored as number
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.children) {
                            val item = snapshot.getValue(Item::class.java)
                            if (item != null) {
                                items.add(item)
                            } else {
                            }
                        }
                        callback(items)
                    } else {
                        callback(emptyList())
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    callback(emptyList())
                }
            })
    }

    var loggedInUserId = 1 //dummy logged in userId
    var exampleFriendUserId = 2011884291 //example friendId for testing

    fun getAllFriendsInfoAndItemInfo() {
        getFriendsUserIds(loggedInUserId) { friendsUserIds ->
            Log.d("MainActivity", "Friends User IDs: $friendsUserIds")
            //get all friends' items
            getUsersInformation(friendsUserIds) { users ->
                Log.d("MainActivity", "Users: $users")
                // get all friends' tems
                getItemsForUsers(friendsUserIds) { items ->
                    Log.d("MainActivity", "Items: $items")
                }
            }
        }
    }

}

