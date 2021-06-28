package com.dicoding.mygithubuserapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dicoding.mygithubuserapp.database.DatabaseContract.FavoriteCol.Companion.TABLE_NAME
import com.dicoding.mygithubuserapp.database.DatabaseContract.FavoriteCol.Companion.USERNAME
import java.sql.SQLException
import kotlin.jvm.Throws

class FavoriteHelper(context: Context) {
    private var dbHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var db: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavoriteHelper? = null

        //initiate database
        fun getInstance(context: Context): FavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open(){
        db = dbHelper.writableDatabase
    }

    fun close(){
        dbHelper.close()
        if (db.isOpen){
            db.close()
        }
    }

    fun queryByUsername(): Cursor {
        return db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "$USERNAME ASC")
    }


    fun insert(user: ContentValues): Long{
        return db.insert(TABLE_NAME, null, user)
    }

    fun deleteByUsername(username: String?): Int{
        return db.delete(TABLE_NAME, USERNAME + "='" + username + "'", null)
    }

}
