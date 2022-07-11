package com.dev.divig.consumerapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UsersFavorite(
    var id: Int = 0,
    var username: String? = "",
    var name: String? = "",
    var avatar: String? = "",
    var location: String? = "",
    var repository: String? = "",
    var followers: String? = "",
    var following: String? = "",
    var company: String? = ""
) : Parcelable