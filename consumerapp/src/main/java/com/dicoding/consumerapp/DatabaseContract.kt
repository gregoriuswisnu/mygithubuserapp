package com.dicoding.consumerapp

import android.net.Uri
import android.provider.BaseColumns

internal object DatabaseContract {

    const val AUTHORITY = "com.dicoding.mygithubuserapp"
    const val SCHEME = "content"

    internal class FavoriteCol: BaseColumns{
        companion object{
            const val TABLE_NAME = "table_favorite"
            const val PROFILE = "img_avatar"
            const val USERNAME = "username"
            const val USER_ID = "id"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()

        }
    }
}