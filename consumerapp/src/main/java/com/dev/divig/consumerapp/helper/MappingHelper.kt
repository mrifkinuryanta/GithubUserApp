package com.dev.divig.consumerapp.helper

import android.database.Cursor
import com.dev.divig.consumerapp.db.DatabaseContract.UsersColumns.Companion.AVATAR
import com.dev.divig.consumerapp.db.DatabaseContract.UsersColumns.Companion.COMPANY
import com.dev.divig.consumerapp.db.DatabaseContract.UsersColumns.Companion.FOLLOWERS
import com.dev.divig.consumerapp.db.DatabaseContract.UsersColumns.Companion.FOLLOWING
import com.dev.divig.consumerapp.db.DatabaseContract.UsersColumns.Companion.LOCATION
import com.dev.divig.consumerapp.db.DatabaseContract.UsersColumns.Companion.NAME
import com.dev.divig.consumerapp.db.DatabaseContract.UsersColumns.Companion.REPOSITORY
import com.dev.divig.consumerapp.db.DatabaseContract.UsersColumns.Companion.USERNAME
import com.dev.divig.consumerapp.db.DatabaseContract.UsersColumns.Companion._ID
import com.dev.divig.consumerapp.models.UsersFavorite

object MappingHelper {
    fun mapCursorToArrayList(noteCursor: Cursor?): ArrayList<UsersFavorite> {
        val usersList = ArrayList<UsersFavorite>()
        noteCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val name = getString(getColumnIndexOrThrow(NAME))
                val avatar = getString(getColumnIndexOrThrow(AVATAR))
                val location = getString(getColumnIndexOrThrow(LOCATION))
                val repository = getString(getColumnIndexOrThrow(REPOSITORY))
                val followers = getString(getColumnIndexOrThrow(FOLLOWERS))
                val following = getString(getColumnIndexOrThrow(FOLLOWING))
                val company = getString(getColumnIndexOrThrow(COMPANY))

                usersList.add(
                    UsersFavorite(
                        id,
                        username,
                        name,
                        avatar,
                        location,
                        repository,
                        followers,
                        following,
                        company
                    )
                )
            }
        }
        return usersList
    }
}