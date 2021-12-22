package com.example.alarm_manager_test

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.alarm_manager_test.databinding.ActivityMainBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object{
        val TAG: String = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var alarmManager: AlarmManager
    private lateinit var mCalender: GregorianCalendar

    private lateinit var notificationManager: NotificationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        mCalender = GregorianCalendar()

        Log.d(TAG, mCalender.time.toString())

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // 알람 버
        binding.button.setOnClickListener {
            setAlarm()
        }


    }

    private fun setAlarm() {

        Log.d(TAG, "setAlarm called : ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())}")

        val receiverIntent = Intent(this, AlarmReceiver::class.java)

        // requestCode를 통해 인텐트를 식별. 취소할 때 참고..
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, receiverIntent, PendingIntent.FLAG_MUTABLE
        )

//        val from: String = "2021-12-21 16:50:00" // 실행전 시간 지정 필요

        // 날짜 포맷을 바꿔주는 소스코드
//        val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//        var datetime: Date
//        try {
//            datetime = dateFormat.parse(from)
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//        datetime = dateFormat.parse(from)

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.YEAR, 2021)
            set(Calendar.MONTH, 12)
            set(Calendar.DAY_OF_MONTH, 22)
            set(Calendar.HOUR_OF_DAY, 14)
            set(Calendar.MINUTE, 23)
            set(Calendar.SECOND, 0)
        }
        // ms 여서 1000을 곱해줘야 초단위로 변환이 됨
        val datetime = System.currentTimeMillis() + 60 * 1000

        alarmManager.set(AlarmManager.RTC_WAKEUP,
            datetime,
            pendingIntent
        )
    }
}