package com.dicoding.mygithubuserapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.dicoding.mygithubuserapp.database.DatabaseContract
import com.dicoding.mygithubuserapp.database.FavoriteHelper

class MyContentProvider : ContentProvider() {

    companion object {
        private const val NOTE = 1
        private const val NOTE_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var consumerFav: FavoriteHelper

        init {
            sUriMatcher.addURI(
                DatabaseContract.AUTHORITY,
                DatabaseContract.FavoriteCol.TABLE_NAME, NOTE
            )

            sUriMatcher.addURI(
                DatabaseContract.AUTHORITY,
                "${DatabaseContract.FavoriteCol.TABLE_NAME}/#",NOTE_ID
            )
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun onCreate(): Boolean {
        consumerFav = FavoriteHelper.getInstance(context as Context)
        consumerFav.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)){
            NOTE -> consumerFav.queryByUsername()
            NOTE_ID -> consumerFav.queryByUsername()
            else -> null
        }    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }
}