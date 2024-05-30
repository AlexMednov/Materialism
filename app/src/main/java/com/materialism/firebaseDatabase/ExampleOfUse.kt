package com.materialism.firebaseDatabase
import com.google.firebase.database.FirebaseDatabase
import com.materialism.firebaseDatabase.data.User

class ExampleOfUse {

    fun writeData() {
        val database = FirebaseDatabase.getInstance().reference
        val message = User()
        database.child("User").push().setValue(message)
    } //Example of how to input data into
}
