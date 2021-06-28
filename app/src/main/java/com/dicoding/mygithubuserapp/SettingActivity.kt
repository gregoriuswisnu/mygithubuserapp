package com.dicoding.mygithubuserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : AppCompatActivity() {

    private lateinit var alarmUser: AlarmUser
    private lateinit var receiver: AlarmReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        alarmUser = AlarmUser()

        val actionBar = supportActionBar

        supportActionBar?.title = "Settings"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        val alarmPreference = AlarmReminder(this)
        toggle_btn.isChecked = alarmPreference.getAlarm().alarmSet
        receiver = AlarmReceiver()
        toggle_btn.setOnCheckedChangeListener { _, it ->
            if (it){
                alarmUser.alarmSet = true
                alarmPreference.setAlarm(alarmUser)
                receiver.setRepeatAlarm(this,AlarmReceiver.TYPE ,"09:00",AlarmReceiver.MESSAGE)
            }else{
                alarmUser.alarmSet = false
                alarmPreference.setAlarm(alarmUser)
                receiver.cancelRepeatAlarm(this, AlarmReceiver.TYPE)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}