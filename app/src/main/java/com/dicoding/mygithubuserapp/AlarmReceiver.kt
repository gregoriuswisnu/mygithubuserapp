package com.dicoding.mygithubuserapp

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object{
        private const val REPEAT_CODE = 101
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "Alarm Submission 3"
        private const val NOTIFICATION_ID = 1
        private const val TIME_FORMAT = "HH:mm"
        const val CONTEXT = "Open Your Github APP"
        const val MESSAGE = "message"
        const val TYPE = "type"
    }


    override fun onReceive(context: Context, intent: Intent) {
        sendNotif(context)
    }

    private fun sendNotif(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, REPEAT_CODE,intent,0)

        val notifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle(CHANNEL_NAME)
                .setContentText(CONTEXT)
                .setSound(alarmSound)
                .setAutoCancel(true)

        //VERSI OREO+ NOTIFICATION CHANNEL

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT)

            builder.setChannelId(CHANNEL_ID)
            notifManager.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notifManager.notify(NOTIFICATION_ID,notification)

    }

    fun setRepeatAlarm(context: Context,type: String, time: String,message: String){
        if(invalidDate(time, TIME_FORMAT))return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context,AlarmReceiver::class.java)
        intent.putExtra(MESSAGE,message)
        intent.putExtra(TYPE,type)
        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE,Integer.parseInt(timeArray[1]))
        val pendingIntent = PendingIntent.getBroadcast(context, REPEAT_CODE, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)
    }

    private fun invalidDate(time: String, timeFormat: String): Boolean {
        return try {
            val df = SimpleDateFormat(timeFormat, Locale.getDefault())
            df.isLenient = false
            df.parse(time)
            false
        } catch (e: ParseException){
            true
        }
    }

    fun cancelRepeatAlarm(context: Context, type: String){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val cancelIntent = Intent(context,AlarmReceiver::class.java)
        val requestCode = REPEAT_CODE
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, cancelIntent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }
}