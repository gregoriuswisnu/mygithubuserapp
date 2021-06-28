package com.dicoding.mygithubuserapp

import android.database.Cursor
import com.dicoding.mygithubuserapp.database.DatabaseContract

object MappingHelper {

    fun mapCursorToArrayList(noteCursor: Cursor?): ArrayList<User>{
        val favList = ArrayList<User>()

        noteCursor?.apply {
            while (moveToNext()){
                val id = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteCol.USER_ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteCol.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteCol.PROFILE))

                favList.add(User(avatar, username, id))
            }
        }
        return favList
    }
}