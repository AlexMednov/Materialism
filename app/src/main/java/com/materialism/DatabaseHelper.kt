package com.materialism

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "inventory.db"
        const val DATABASE_VERSION = 1

        // User table
        const val TABLE_USER = "User"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USER_NAME = "name"
        const val COLUMN_USER_EMAIL = "emailAddress"
        const val COLUMN_USER_REGISTERED = "isRegistered"

        // Category table
        const val TABLE_CATEGORY = "Category"
        const val COLUMN_CATEGORY_ID = "id"
        const val COLUMN_CATEGORY_NAME = "name"
        const val COLUMN_CATEGORY_DESCRIPTION = "description"
        const val COLUMN_CATEGORY_DEFAULT = "isDefault"

        // Subcategory table
        const val TABLE_SUBCATEGORY = "Subcategory"
        const val COLUMN_SUBCATEGORY_ID = "id"
        const val COLUMN_SUBCATEGORY_NAME = "name"
        const val COLUMN_SUBCATEGORY_CATEGORY_ID = "category_id"

        // Item table
        const val TABLE_ITEM = "Item"
        const val COLUMN_ITEM_ID = "id"
        const val COLUMN_ITEM_NAME = "name"
        const val COLUMN_ITEM_DESCRIPTION = "description"
        const val COLUMN_ITEM_LOCATION = "location"
        const val COLUMN_ITEM_PUBLIC = "isPublic"
        const val COLUMN_ITEM_LOANED = "isLoaned"
        const val COLUMN_ITEM_DATE_ADDED = "dateTimeAdded"
        const val COLUMN_ITEM_DATE_MODIFIED = "dateTimeModified"
        const val COLUMN_ITEM_USER_ID = "userId"
        const val COLUMN_ITEM_CATEGORY_ID = "categoryId"
        const val COLUMN_ITEM_SUBCATEGORY_ID = "subcategoryId"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable = """
            CREATE TABLE $TABLE_USER (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_NAME TEXT NOT NULL,
                $COLUMN_USER_EMAIL TEXT,
                $COLUMN_USER_REGISTERED BOOLEAN NOT NULL
            );
        """

        val createCategoryTable = """
            CREATE TABLE $TABLE_CATEGORY (
                $COLUMN_CATEGORY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CATEGORY_NAME TEXT NOT NULL,
                $COLUMN_CATEGORY_DESCRIPTION TEXT,
                $COLUMN_CATEGORY_DEFAULT BOOLEAN NOT NULL
            );
        """

        val createSubcategoryTable = """
            CREATE TABLE $TABLE_SUBCATEGORY (
                $COLUMN_SUBCATEGORY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SUBCATEGORY_NAME TEXT NOT NULL,
                $COLUMN_SUBCATEGORY_CATEGORY_ID INTEGER NOT NULL,
                FOREIGN KEY($COLUMN_SUBCATEGORY_CATEGORY_ID) REFERENCES $TABLE_CATEGORY($COLUMN_CATEGORY_ID)
            );
        """

        val createItemTable = """
            CREATE TABLE $TABLE_ITEM (
                $COLUMN_ITEM_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ITEM_NAME TEXT NOT NULL,
                $COLUMN_ITEM_DESCRIPTION TEXT,
                $COLUMN_ITEM_LOCATION TEXT,
                $COLUMN_ITEM_PUBLIC BOOLEAN NOT NULL,
                $COLUMN_ITEM_LOANED BOOLEAN NOT NULL,
                $COLUMN_ITEM_DATE_ADDED DATETIME NOT NULL,
                $COLUMN_ITEM_DATE_MODIFIED DATETIME NOT NULL,
                $COLUMN_ITEM_USER_ID INTEGER NOT NULL,
                $COLUMN_ITEM_CATEGORY_ID INTEGER NOT NULL,
                $COLUMN_ITEM_SUBCATEGORY_ID INTEGER,
                FOREIGN KEY($COLUMN_ITEM_USER_ID) REFERENCES $TABLE_USER($COLUMN_USER_ID),
                FOREIGN KEY($COLUMN_ITEM_CATEGORY_ID) REFERENCES $TABLE_CATEGORY($COLUMN_CATEGORY_ID),
                FOREIGN KEY($COLUMN_ITEM_SUBCATEGORY_ID) REFERENCES $TABLE_SUBCATEGORY($COLUMN_SUBCATEGORY_ID)
            );
        """

        db.execSQL(createUserTable)
        db.execSQL(createCategoryTable)
        db.execSQL(createSubcategoryTable)
        db.execSQL(createItemTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ITEM")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SUBCATEGORY")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORY")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        onCreate(db)
    }
}
