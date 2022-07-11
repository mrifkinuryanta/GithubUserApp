package com.dev.divig.githubuserapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var username: String,
    var name: String,
    var avatar: String,
    var location: String,
    var repository: String,
    var followers: String,
    var following: String,
    var company: String
) : Parcelable