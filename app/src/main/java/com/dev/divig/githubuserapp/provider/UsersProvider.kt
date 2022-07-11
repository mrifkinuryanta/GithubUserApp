package com.dev.divig.githubuserapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.dev.divig.githubuserapp.db.DatabaseContract.AUTHORITY
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.CONTENT_URI
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.TABLE_NAME
import com.dev.divig.githubuserapp.db.UsersFavoriteHelper

class UsersProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        usersFavoriteHelper = UsersFavoriteHelper.getInstance(context as Context)
        usersFavoriteHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            USERS -> usersFavoriteHelper.queryAll()
            USERS_ID -> usersFavoriteHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (USERS) {
            sUriMatcher.match(uri) -> usersFavoriteHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val update: Int = when (USERS_ID) {
            sUriMatcher.match(uri) -> usersFavoriteHelper.update(
                uri.lastPathSegment.toString(),
                values
            )
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return update
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val delete: Int = when (USERS_ID) {
            sUriMatcher.match(uri) -> usersFavoriteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return delete
    }

    companion object {
        private const val USERS = 1
        private const val USERS_ID = 2

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var usersFavoriteHelper: UsersFavoriteHelper

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USERS)

            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/*", USERS_ID)
        }
    }
}
