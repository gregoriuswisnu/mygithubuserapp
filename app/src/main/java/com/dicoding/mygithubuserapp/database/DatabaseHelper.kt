package com.dicoding.mygithubuserapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dicoding.mygithubuserapp.database.DatabaseContract.FavoriteCol.Companion.PROFILE
import com.dicoding.mygithubuserapp.database.DatabaseContract.FavoriteCol.Companion.TABLE_NAME
import com.dicoding.mygithubuserapp.database.DatabaseContract.FavoriteCol.Companion.USERNAME
import com.dicoding.mygithubuserapp.database.DatabaseContract.FavoriteCol.Companion.USER_ID

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "databaseFavorit"
        private const val DATABASE_VERSION = 1
        private val CREATE_TABLE_FAVORITE = "create table $TABLE_NAME" +
                "($USER_ID integer primary key autoincrement," +
                "$USERNAME text not null," +
                "$PROFILE text no null);"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}