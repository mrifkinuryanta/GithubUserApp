package com.dev.divig.consumerapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Followers(
    var username: String,
    var avatar: String
) : Parcelable