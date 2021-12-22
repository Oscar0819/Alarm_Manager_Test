package com.example.alarm_manager_test

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat

class AlarmReceiver: BroadcastReceiver() {

    companion object {
        val TAG: String = "AlarmReceiber"
        private val CHANNEL_ID: String = "Channel1"
        private val CHANNEL_NAME: String = "Channel1"
    }

    lateinit var manager: NotificationManager
    lateinit var builder: NotificationCompat.Builder

//    init {
//        val manager: NotificationManager
//        val builder: NotificationCompat.Builder
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive called : ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())}")
        val am: AlarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (context != null) {
            manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )
        )
        builder = NotificationCompat.Builder(context, CHANNEL_ID)

        // 알림창 클릭시 activity call
        val intent2: Intent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 101, intent2, PendingIntent.FLAG_MUTABLE
        )

        // 노티 제목
        builder.setContentTitle("알람")
        builder.setSmallIcon(R.drawable.ic_launcher_background)
        // 노티 터치시 자동 삭제
        builder.setAutoCancel(true)
        builder.setContentIntent(pendingIntent)

        val notification: Notification = builder.build()
        manager.notify(1, notification)

    }
}