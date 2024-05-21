package com.materialism

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class DatabaseManager(context: Context) {

    private val dbHelper = DatabaseHelper(context)
    private var database: SQLiteDatabase? = null

    fun open() {
        database = dbHelper.writableDatabase
    }

    fun close() {
        dbHelper.close()
    }

    // User CRUD operations
    fun addUser(name: String, email: String?, isRegistered: Boolean): Long {
        val values = ContentValues()
        values.put(DatabaseHelper.COLUMN_USER_NAME, name)
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, email)
        values.put(DatabaseHelper.COLUMN_USER_REGISTERED, isRegistered)
        return database!!.insert(DatabaseHelper.TABLE_USER, null, values)
    }

    fun getUser(id: Int): Cursor {
        val columns = arrayOf(
            DatabaseHelper.COLUMN_USER_ID,
            DatabaseHelper.COLUMN_USER_NAME,
            DatabaseHelper.COLUMN_USER_EMAIL,
            DatabaseHelper.COLUMN_USER_REGISTERED
        )
        return database!!.query(
            DatabaseHelper.TABLE_USER, columns, "${DatabaseHelper.COLUMN_USER_ID} =?",
            arrayOf(id.toString()), null, null, null
        )
    }

    fun updateUser(id: Int, name: String, email: String?, isRegistered: Boolean): Int {
        val values = ContentValues()
        values.put(DatabaseHelper.COLUMN_USER_NAME, name)
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, email)
        values.put(DatabaseHelper.COLUMN_USER_REGISTERED, isRegistered)
        return database!!.update(DatabaseHelper.TABLE_USER, values, "${DatabaseHelper.COLUMN_USER_ID} =?", arrayOf(id.toString()))
    }

    fun deleteUser(id: Int): Int {
        return database!!.delete(DatabaseHelper.TABLE_USER, "${DatabaseHelper.COLUMN_USER_ID} =?", arrayOf(id.toString()))
    }

    // Category CRUD operations
    fun addCategory(name: String, description: String?, isDefault: Boolean): Long {
        val values = ContentValues()
        values.put(DatabaseHelper.COLUMN_CATEGORY_NAME, name)
        values.put(DatabaseHelper.COLUMN_CATEGORY_DESCRIPTION, description)
        values.put(DatabaseHelper.COLUMN_CATEGORY_DEFAULT, isDefault)
        return database!!.insert(DatabaseHelper.TABLE_CATEGORY, null, values)
    }

    fun getCategory(id: Int): Cursor {
        val columns = arrayOf(
            DatabaseHelper.COLUMN_CATEGORY_ID,
            DatabaseHelper.COLUMN_CATEGORY_NAME,
            DatabaseHelper.COLUMN_CATEGORY_DESCRIPTION,
            DatabaseHelper.COLUMN_CATEGORY_DEFAULT
        )
        return database!!.query(
            DatabaseHelper.TABLE_CATEGORY, columns, "${DatabaseHelper.COLUMN_CATEGORY_ID} =?",
            arrayOf(id.toString()), null, null, null
        )
    }

    fun updateCategory(id: Int, name: String, description: String?, isDefault: Boolean): Int {
        val values = ContentValues()
        values.put(DatabaseHelper.COLUMN_CATEGORY_NAME, name)
        values.put(DatabaseHelper.COLUMN_CATEGORY_DESCRIPTION, description)
        values.put(DatabaseHelper.COLUMN_CATEGORY_DEFAULT, isDefault)
        return database!!.update(DatabaseHelper.TABLE_CATEGORY, values, "${DatabaseHelper.COLUMN_CATEGORY_ID} =?", arrayOf(id.toString()))
    }

    fun deleteCategory(id: Int): Int {
        return database!!.delete(DatabaseHelper.TABLE_CATEGORY, "${DatabaseHelper.COLUMN_CATEGORY_ID} =?", arrayOf(id.toString()))
    }

    // Subcategory CRUD operations
    fun addSubcategory(name: String, categoryId: Int): Long {
        val values = ContentValues()
        values.put(DatabaseHelper.COLUMN_SUBCATEGORY_NAME, name)
        values.put(DatabaseHelper.COLUMN_SUBCATEGORY_CATEGORY_ID, categoryId)
        return database!!.insert(DatabaseHelper.TABLE_SUBCATEGORY, null, values)
    }

    fun getSubcategory(id: Int): Cursor {
        val columns = arrayOf(
            DatabaseHelper.COLUMN_SUBCATEGORY_ID,
            DatabaseHelper.COLUMN_SUBCATEGORY_NAME,
            DatabaseHelper.COLUMN_SUBCATEGORY_CATEGORY_ID
        )
        return database!!.query(
            DatabaseHelper.TABLE_SUBCATEGORY, columns, "${DatabaseHelper.COLUMN_SUBCATEGORY_ID} =?",
            arrayOf(id.toString()), null, null, null
        )
    }

    fun updateSubcategory(id: Int, name: String, categoryId: Int): Int {
        val values = ContentValues()
        values.put(DatabaseHelper.COLUMN_SUBCATEGORY_NAME, name)
        values.put(DatabaseHelper.COLUMN_SUBCATEGORY_CATEGORY_ID, categoryId)
        return database!!.update(DatabaseHelper.TABLE_SUBCATEGORY, values, "${DatabaseHelper.COLUMN_SUBCATEGORY_ID} =?", arrayOf(id.toString()))
    }

    fun deleteSubcategory(id: Int): Int {
        return database!!.delete(DatabaseHelper.TABLE_SUBCATEGORY, "${DatabaseHelper.COLUMN_SUBCATEGORY_ID} =?", arrayOf(id.toString()))
    }

    // Item CRUD operations
    fun addItem(
        name: String, imageURI: String, description: String?, location: String?,
        isPublic: Boolean, isLoaned: Boolean, dateTimeAdded: String,
        dateTimeModified: String, userId: Int, categoryId: Int, subcategoryId: Int?
    ): Long {
        val values = ContentValues()
        values.put(DatabaseHelper.COLUMN_ITEM_NAME, name)
        values.put(DatabaseHelper.COLUMN_ITEM_IMAGE_URI, imageURI)
        values.put(DatabaseHelper.COLUMN_ITEM_DESCRIPTION, description)
        values.put(DatabaseHelper.COLUMN_ITEM_LOCATION, location)
        values.put(DatabaseHelper.COLUMN_ITEM_PUBLIC, isPublic)
        values.put(DatabaseHelper.COLUMN_ITEM_LOANED, isLoaned)
        values.put(DatabaseHelper.COLUMN_ITEM_DATE_ADDED, dateTimeAdded)
        values.put(DatabaseHelper.COLUMN_ITEM_DATE_MODIFIED, dateTimeModified)
        values.put(DatabaseHelper.COLUMN_ITEM_USER_ID, userId)
        values.put(DatabaseHelper.COLUMN_ITEM_CATEGORY_ID, categoryId)
        values.put(DatabaseHelper.COLUMN_ITEM_SUBCATEGORY_ID, subcategoryId)
        return database!!.insert(DatabaseHelper.TABLE_ITEM, null, values)
    }

    fun getItem(id: Int): Cursor {
        val columns = arrayOf(
            DatabaseHelper.COLUMN_ITEM_ID,
            DatabaseHelper.COLUMN_ITEM_NAME,
            DatabaseHelper.COLUMN_ITEM_IMAGE_URI,
            DatabaseHelper.COLUMN_ITEM_DESCRIPTION,
            DatabaseHelper.COLUMN_ITEM_LOCATION,
            DatabaseHelper.COLUMN_ITEM_PUBLIC,
            DatabaseHelper.COLUMN_ITEM_LOANED,
            DatabaseHelper.COLUMN_ITEM_DATE_ADDED,
            DatabaseHelper.COLUMN_ITEM_DATE_MODIFIED,
            DatabaseHelper.COLUMN_ITEM_USER_ID,
            DatabaseHelper.COLUMN_ITEM_CATEGORY_ID,
            DatabaseHelper.COLUMN_ITEM_SUBCATEGORY_ID
        )
        return database!!.query(
            DatabaseHelper.TABLE_ITEM, columns, "${DatabaseHelper.COLUMN_ITEM_ID} =?",
            arrayOf(id.toString()), null, null, null
        )
    }

    fun updateItem(
        id: Int, name: String, imageURI: String, description: String?, location: String?,
        isPublic: Boolean, isLoaned: Boolean, dateTimeAdded: String,
        dateTimeModified: String, userId: Int, categoryId: Int, subcategoryId: Int?
    ): Int {
        val values = ContentValues()
        values.put(DatabaseHelper.COLUMN_ITEM_NAME, name)
        values.put(DatabaseHelper.COLUMN_ITEM_IMAGE_URI, imageURI)
        values.put(DatabaseHelper.COLUMN_ITEM_DESCRIPTION, description)
        values.put(DatabaseHelper.COLUMN_ITEM_LOCATION, location)
        values.put(DatabaseHelper.COLUMN_ITEM_PUBLIC, isPublic)
        values.put(DatabaseHelper.COLUMN_ITEM_LOANED, isLoaned)
        values.put(DatabaseHelper.COLUMN_ITEM_DATE_ADDED, dateTimeAdded)
        values.put(DatabaseHelper.COLUMN_ITEM_DATE_MODIFIED, dateTimeModified)
        values.put(DatabaseHelper.COLUMN_ITEM_USER_ID, userId)
        values.put(DatabaseHelper.COLUMN_ITEM_CATEGORY_ID, categoryId)
        values.put(DatabaseHelper.COLUMN_ITEM_SUBCATEGORY_ID, subcategoryId)
        return database!!.update(DatabaseHelper.TABLE_ITEM, values, "${DatabaseHelper.COLUMN_ITEM_ID} =?", arrayOf(id.toString()))
    }

    fun deleteItem(id: Int): Int {
        return database!!.delete(DatabaseHelper.TABLE_ITEM, "${DatabaseHelper.COLUMN_ITEM_ID} =?", arrayOf(id.toString()))
    }
}
