package com.sirionrazzer.diary.settings

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import java.util.Calendar

object NotificationHelper {
    const val ALARM_TYPE_RTC = 100 // pending intent identifier
    private var alarmManagerRTC: AlarmManager? = null
    private var alarmIntentRTC: PendingIntent? = null

    fun scheduleRepeatingRTCNotification(
        context: Context,
        hour: Int,
        min: Int
    ) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("hour", hour)
        intent.putExtra("min", min)

        alarmIntentRTC = PendingIntent.getBroadcast(
            context,
            ALARM_TYPE_RTC,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManagerRTC =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, min)
        }
        alarmManagerRTC!!.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntentRTC
        )
    }

    fun cancelAlarmRTC(context: Context, hour: Int, min: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("hour", hour)
        intent.putExtra("min", min)
        alarmIntentRTC = PendingIntent.getBroadcast(
            context,
            ALARM_TYPE_RTC,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        if (alarmManagerRTC != null) alarmManagerRTC!!.cancel(alarmIntentRTC)
    }

    fun getNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun enableBootReceiver(context: Context) {
        val receiver = ComponentName(context, AlarmBootReceiver::class.java)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    fun disableBootReceiver(context: Context) {
        val receiver = ComponentName(context, AlarmBootReceiver::class.java)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}