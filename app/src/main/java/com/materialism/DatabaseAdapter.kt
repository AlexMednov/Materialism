package com.materialism

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.materialism.firebaseDatabase.data.Category
import com.materialism.firebaseDatabase.data.Item
import com.materialism.firebaseDatabase.data.Quest
import com.materialism.firebaseDatabase.data.QuestItem
import com.materialism.firebaseDatabase.data.SubCategory
import com.materialism.firebaseDatabase.data.User

class DatabaseAdapter(val databaseManager: DatabaseManager) {

  private val firebaseDatabase = FirebaseDatabase.getInstance().reference

  // Get categories from Firebase and write them to SQLite if they do not already exist
  fun syncCategories() {
    firebaseDatabase
        .child("Category")
        .get()
        .addOnSuccessListener { dataSnapshot ->
          dataSnapshot.children.forEach { categorySnapshot ->
            try {
              val category = categorySnapshot.getValue(Category::class.java)
              if (category != null) {
                val localCategoryCursor = databaseManager.getCategory(category.id)
                if (!localCategoryCursor.moveToFirst()) {
                  databaseManager.addCategory(
                      category.name, category.description, category.isDefault)
                }
              }
            } catch (e: Exception) {}
          }
        }
        .addOnFailureListener { exception -> }
  }

  // Get subcategories from Firebase and write them to SQLite if they do not already exist
  fun syncSubCategories() {
    firebaseDatabase
        .child("SubCategory")
        .get()
        .addOnSuccessListener { dataSnapshot ->
          dataSnapshot.children.forEach { subcategorySnapshot ->
            try {
              val subcategory = subcategorySnapshot.getValue(SubCategory::class.java)
              if (subcategory != null) {
                val localSubCategoryCursor = databaseManager.getSubcategory(subcategory.id)
                if (!localSubCategoryCursor.moveToFirst()) {
                  databaseManager.addSubcategory(subcategory.name, subcategory.categoryId)
                }
              }
            } catch (e: Exception) {}
          }
        }
        .addOnFailureListener { exception -> }
  }

  fun syncQuests() {
    firebaseDatabase
        .child("Quests")
        .get()
        .addOnSuccessListener { dataSnapshot ->
          dataSnapshot.children.forEach { questSnapshot ->
            try {
              val quest = questSnapshot.getValue(Quest::class.java)
              if (quest != null) {
                val localQuestCursor = databaseManager.getQuest(quest.id)
                if (!localQuestCursor.moveToFirst()) {
                  databaseManager.addQuest(quest.type, quest.weight, quest.categoryId)
                }
              }
            } catch (e: Exception) {}
          }
        }
        .addOnFailureListener { exception ->
          // Handle any errors
        }
  }

  fun syncQuestItems() {
    firebaseDatabase
        .child("QuestItems")
        .get()
        .addOnSuccessListener { dataSnapshot ->
          dataSnapshot.children.forEach { questItemSnapshot ->
            try {
              val questItem = questItemSnapshot.getValue(QuestItem::class.java)
              if (questItem != null) {
                val localQuestItemCursor = databaseManager.getQuestItem(questItem.id)
                if (!localQuestItemCursor.moveToFirst()) {
                  databaseManager.addQuestItem(questItem.name, questItem.categoryId)
                }
              }
            } catch (e: Exception) {}
          }
        }
        .addOnFailureListener { exception ->
          // Handle any errors
        }
  }

  fun addItem(item: Item, userId: Int) {
    if (item.isPublic) {
      firebaseDatabase.child("User").child(userId.toString()).child("Item").push().setValue(item)
    }
  }

  fun updateItem(item: Item, userId: Int) {
    if (item.isPublic) {
      firebaseDatabase
          .child("Users")
          .child(userId.toString())
          .child("Items")
          .child(item.id.toString())
          .setValue(item)
    }
  }

  fun deleteItem(itemId: Int, userId: Int) {
    firebaseDatabase
        .child("User")
        .child(userId.toString())
        .child("Item")
        .child(itemId.toString())
        .removeValue()
  }

  fun deleteItem(itemId: Int, userId: String) {
    firebaseDatabase
        .child("Users")
        .child(userId)
        .child("Items")
        .child(itemId.toString())
        .removeValue()
  }

  fun getFriendsUserIds(loggedInUserId: Int, callback: (List<Int>) -> Unit) {
    val databaseReference = FirebaseDatabase.getInstance().getReference("Friend")

    databaseReference.addListenerForSingleValueEvent(
        object : ValueEventListener {
          override fun onDataChange(dataSnapshot: DataSnapshot) {
            val friendsUserIds = mutableListOf<Int>()

            for (snapshot in dataSnapshot.children) {
              // Getting the friends map
              val friendMap = snapshot.value as? Map<String, Long>
              if (friendMap != null) {
                val userId1 = friendMap["userId1"]?.toInt()
                val userId2 = friendMap["userId2"]?.toInt()
                Log.d("MainActivity", "UserId1: $userId1, UserId2: $userId2")
                if (userId1 != null && userId2 != null) {
                  if (userId1 == loggedInUserId && userId2 != loggedInUserId) {
                    friendsUserIds.add(userId2)
                  } else if (userId2 == loggedInUserId && userId1 != loggedInUserId) {
                    friendsUserIds.add(userId1)
                  }
                }
              }
            }
            callback(friendsUserIds)
          }

          override fun onCancelled(databaseError: DatabaseError) {
            // Handle error
          }
        })
  }

  // passes, however all users are retrieved before filtering
  fun getUsersInformation(userIds: List<Int>, callback: (List<User>) -> Unit) {
    val databaseReference = FirebaseDatabase.getInstance().getReference("User")
    val users = mutableListOf<User>()
    val userIdSet = userIds.toSet() // Use a set for faster lookups

    databaseReference.addListenerForSingleValueEvent(
        object : ValueEventListener {
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

  // passes
  fun getItemsForUsers(userIds: List<Int>, callback: (List<Item>) -> Unit) {
    val databaseReference = FirebaseDatabase.getInstance().getReference("Item")
    val items = mutableListOf<Item>()

    userIds.forEach { userId ->
      databaseReference
          .orderByChild("userId")
          .equalTo(userId.toString())
          .addListenerForSingleValueEvent(
              object : ValueEventListener {
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

  // passes
  fun getSingleUserInformation(userId: Int, callback: (User?) -> Unit) {
    val databaseReference = FirebaseDatabase.getInstance().getReference("User")

    databaseReference
        .orderByChild("id")
        .equalTo(userId.toDouble())
        .addListenerForSingleValueEvent(
            object : ValueEventListener {
              override fun onDataChange(dataSnapshot: DataSnapshot) {

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
                callback(null)
              }
            })
  }

  // passes, but crashes due to there being no No-Argument Constructors for data classes
  fun getItemsForSingleUser(userId: Int, callback: (List<Item>) -> Unit) {
    val databaseReference = FirebaseDatabase.getInstance().getReference("Item")
    val items = mutableListOf<Item>()

    databaseReference
        .orderByChild("userId")
        .equalTo(userId.toDouble()) // Assuming userId is stored as number
        .addListenerForSingleValueEvent(
            object : ValueEventListener {
              override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                  for (snapshot in dataSnapshot.children) {
                    val item = snapshot.getValue(Item::class.java)
                    if (item != null) {
                      items.add(item)
                    } else {}
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

  var loggedInUserId = 1 // dummy logged in userId
  var exampleFriendUserId = 2011884291 // example friendId for testing

  fun getAllFriendsInfoAndItemInfo() {
    getFriendsUserIds(loggedInUserId) { friendsUserIds ->
      // get all friends' items
      getUsersInformation(friendsUserIds) { users ->
        // get all friends' tems
        getItemsForUsers(friendsUserIds) { items -> }
      }
    }
  }
}
