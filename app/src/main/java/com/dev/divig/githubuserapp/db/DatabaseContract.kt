package com.dev.divig.githubuserapp.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val SCHEME = "content"
    const val AUTHORITY = "com.dev.divig.githubuserapp"

    class UsersColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "users"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val NAME = "name"
            const val AVATAR = "avatar"
            const val LOCATION = "location"
            const val REPOSITORY = "repository"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"
            const val COMPANY = "company"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}