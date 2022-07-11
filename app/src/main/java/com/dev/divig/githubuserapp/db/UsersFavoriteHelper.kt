package com.dev.divig.githubuserapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.TABLE_NAME
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.USERNAME
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion._ID
import java.sql.SQLException

class UsersFavoriteHelper(context: Context) {

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private lateinit var database: SQLiteDatabase
        private var INSTANCE: UsersFavoriteHelper? = null
        fun getInstance(context: Context): UsersFavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UsersFavoriteHelper(context)
            }
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$USERNAME = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$USERNAME = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$USERNAME = ?", arrayOf(id))
    }
}