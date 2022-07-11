package com.dev.divig.githubuserapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Following(
    var username: String,
    var avatar: String
) : Parcelable