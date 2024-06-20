package com.materialism.database.firebaseDatabase

import com.google.firebase.database.FirebaseDatabase
import com.materialism.database.firebaseDatabase.data.Category

class ExampleOfUse {

  fun writeData() {
    val database = FirebaseDatabase.getInstance().reference
    val message = Category()
    database.child("Category").push().setValue(message)
  } // Example of how to input data into
}
