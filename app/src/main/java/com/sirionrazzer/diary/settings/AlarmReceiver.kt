package com.sirionrazzer.diary.settings

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.boarding.BoardingActivity
import com.sirionrazzer.diary.settings.NotificationHelper.getNotificationManager

class AlarmReceiver : BroadcastReceiver() {
    private val CHANNEL_ID: String = "Dr. Diary reminder"

    override fun onReceive(
        context: Context?,
        intent: Intent?
    ) {
        val intentToRepeat = Intent(context, BoardingActivity::class.java)
        intentToRepeat.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(
            context,
            NotificationHelper.ALARM_TYPE_RTC,
            intentToRepeat,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val repeatedNotification: Notification =
            buildLocalNotification(context, pendingIntent).build()
        getNotificationManager(context!!)
            .notify(NotificationHelper.ALARM_TYPE_RTC, repeatedNotification)
    }

    private fun buildLocalNotification(
        context: Context?,
        pendingIntent: PendingIntent?
    ): NotificationCompat.Builder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context!!)
        }

        val builder = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_edit_white_24dp)
            .setContentTitle(context.getString(R.string.reminder_title))
            .setContentText(context.getString(R.string.reminder_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        return builder as NotificationCompat.Builder
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.resources.getString(R.string.channel_name)
            val descriptionText = context.resources.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.BLUE
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}