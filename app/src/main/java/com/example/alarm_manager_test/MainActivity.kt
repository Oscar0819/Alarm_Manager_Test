package com.example.alarm_manager_test

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import com.example.alarm_manager_test.MainActivity.Companion.TAG
import com.example.alarm_manager_test.databinding.ActivityMainBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

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

        // 알람 버튼
        binding.button.setOnClickListener {
            // ms 여서 1000을 곱해줘야 초단위로 변환이 됨
            setAlarm(1000, 1)
        }

        binding.button2.setOnClickListener {
            setAlarm(3000, 2)
        }

        binding.button3.setOnClickListener {
            setAlarm(6000, 3)
        }

        binding.button4.setOnClickListener {
            // 그레고리력..
            val today = GregorianCalendar()
            val year0: Int = today.get(Calendar.YEAR)
            val month0: Int = today.get(Calendar.MONTH)
            val date0: Int = today.get(Calendar.DATE)

            // 날짜 선택 다이얼로그
            val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    val selectDate: String = "${year}-${month+1}-${dayOfMonth}"
                    val todayDate: String = "${today.get(Calendar.YEAR)}-${today.get(Calendar.MONTH) + 1}-${today.get(Calendar.DATE)}"

                    Log.d(TAG, "선택 날짜 : ${selectDate}")
                    Log.d(TAG, "오늘 날짜 : ${todayDate}")

                    val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
                    //------------------------------------------
                    val FirstDate: Date = try {
                       dateFormat.parse(todayDate)
                    } catch (e: ParseException) {
                        Date()
                    }
                    Log.d(TAG, "FirstDate : ${FirstDate}")
                    val SecondDate: Date = try {
                        dateFormat.parse(selectDate)
                    } catch (e: ParseException) {
                        Date()
                    }

                    Log.d(TAG, "SecondDate : ${SecondDate}")
                    val calDate: Long = FirstDate.time - SecondDate.time
                    Log.d(TAG, "calDate : ${calDate}")

                    var calDateDays: Long = calDate / (24*60*60*1000)
                    Log.d(TAG, "calDateDays : ${calDateDays}")

                    calDateDays = abs(calDateDays)

                    Log.d(TAG, "두 날짜의 차이 : ${calDateDays}")
                    //--------------------------------------------

                    binding.button4.setText("${year}-${month+1}-${dayOfMonth}")
                }
            }, year0, month0, date0)
            dlg.show()
        }

        binding.cancelbtn.setOnClickListener {
            Log.d(TAG, "amCancel called")
            val am: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val intent: Intent = Intent(this, AlarmReceiver::class.java)
            val sender: PendingIntent = PendingIntent.getBroadcast(this,
                2,
                intent,
                PendingIntent.FLAG_MUTABLE)
            if (sender != null) {
                am.cancel(sender)
                sender.cancel()
            }
        }

        binding.checkbtn.setOnClickListener {
            Log.d(TAG, "checkbtn called")

            val intent: Intent = Intent(this, AlarmReceiver::class.java)
            val sender: PendingIntent = PendingIntent.getBroadcast(this,
                2,
                intent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_NO_CREATE)

            if (sender == null) {
                // 알람이 없는 경우
                Log.d(TAG, "예약된 알람이 없습니다")
            } else {
                // 알람이 있는 경우
                Log.d(TAG, "예약된 알람이 있습니다!")
            }
        }
    }

    private fun setAlarm(datetime: Long, requestCode: Int) {        // ms 여서 1000을 곱해줘야 초단위로 변환이 됨

        Log.d(TAG, "setAlarm called : ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())}")

        val receiverIntent = Intent(this, AlarmReceiver::class.java)
        //receiverIntent.extras.putInt("id", id)
        // requestCode를 통해 인텐트를 식별. 취소할 때 참고..
        val pendingIntent = PendingIntent.getBroadcast(
            this, requestCode, receiverIntent, PendingIntent.FLAG_MUTABLE
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

//        val calendar: Calendar = Calendar.getInstance().apply {
//            timeInMillis = System.currentTimeMillis()
//            set(Calendar.YEAR, 2021)
//            set(Calendar.MONTH, 12)
//            set(Calendar.DAY_OF_MONTH, 22)
//            set(Calendar.HOUR_OF_DAY, 14)
//            set(Calendar.MINUTE, 23)
//            set(Calendar.SECOND, 0)
//        }
        // ms 여서 1000을 곱해줘야 초단위로 변환이 됨
//        val datetime = System.currentTimeMillis() + 10 * 1000

        alarmManager.set(AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 10 * datetime,
            pendingIntent
        )
    }
}