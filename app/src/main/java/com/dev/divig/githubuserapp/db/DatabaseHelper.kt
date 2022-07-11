package com.dev.divig.githubuserapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.AVATAR
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.COMPANY
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.FOLLOWERS
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.FOLLOWING
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.LOCATION
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.NAME
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.REPOSITORY
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.TABLE_NAME
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.USERNAME
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion._ID

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "dbusersfavorite"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $USERNAME TEXT NOT NULL," +
                " $NAME TEXT NOT NULL," +
                " $AVATAR TEXT NOT NULL," +
                " $LOCATION TEXT NOT NULL," +
                " $REPOSITORY TEXT NOT NULL," +
                " $FOLLOWERS TEXT NOT NULL," +
                " $FOLLOWING TEXT NOT NULL," +
                " $COMPANY TEXT NOT NULL)"
    }
}