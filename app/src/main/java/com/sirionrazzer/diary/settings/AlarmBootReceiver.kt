package com.sirionrazzer.diary.settings

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.models.UserStorage
import javax.inject.Inject

class AlarmBootReceiver : BroadcastReceiver() {
    @Inject
    lateinit var userStorage: UserStorage

    init {
        Diary.app.appComponent.inject(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {

            val reminderActive = userStorage.userSettings.reminderActive

            if (reminderActive) {
                NotificationHelper.scheduleRepeatingRTCNotification(
                    context,
                    intent.getIntExtra("hour", userStorage.userSettings.reminderTimeHour),
                    intent.getIntExtra("min", userStorage.userSettings.reminderTimeMinute)
                )
            }
        }
    }
}