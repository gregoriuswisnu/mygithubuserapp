package com.dicoding.mygithubuserapp

import android.content.Context

class AlarmReminder(context: Context) {
    companion object{
        const val TITLE = "pref_alarm"
        private const val REMINDER = "Already Set!"
    }

    private val reminder = context.getSharedPreferences(TITLE,Context.MODE_PRIVATE)

    fun setAlarm(value: AlarmUser){
        val settings = reminder.edit()
        settings.putBoolean(REMINDER,value.alarmSet)
        settings.apply()
    }

    fun getAlarm():AlarmUser{
        val alarmUser = AlarmUser()
        alarmUser.alarmSet = reminder.getBoolean(REMINDER, false)
        return alarmUser
    }
}