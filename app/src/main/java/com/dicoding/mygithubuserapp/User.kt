package com.dicoding.mygithubuserapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    var avatar: String? = null,
    var username: String? = null,
    var id: String? = null,
    var name: String? = null,
    var repository: String? = null,
    var follower: String? = null,
    var following: String? = null
):Parcelable


