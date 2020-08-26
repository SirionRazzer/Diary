package com.sirionrazzer.diary.settings

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.UserStorage
import kotlinx.android.synthetic.main.fragment_reminder_settings.*
import javax.inject.Inject

class ReminderSettingsDialog : DialogFragment() {

    companion object {
        val TAG: String = ReminderSettingsDialog::class.java.simpleName
    }

    @Inject
    lateinit var userStorage: UserStorage

    private var reminderActive: Boolean = false
    private var hour: Int = 12
    private var minute: Int = 0
    private var tmpHour: Int = 13
    private var tmpMinute: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Diary.app.appComponent.inject(this)
        val rootView = inflater.inflate(R.layout.fragment_reminder_settings, container, false)
        dialog?.setTitle(resources.getString(R.string.set_reminder_notification))
        return rootView
    }

    override fun onResume() {
        super.onResume()
        val params = dialog?.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window!!.attributes = params as android.view.WindowManager.LayoutParams

        this.reminderActive = userStorage.userSettings.reminderActive
        this.hour = userStorage.userSettings.reminderTimeHour
        this.minute = userStorage.userSettings.reminderTimeMinute
        this.tmpHour = hour
        this.tmpMinute = minute
        if (!userStorage.userSettings.reminderAdded) {
            swReminder.isEnabled = false
            tvActiveReminderCaption.text = getString(R.string.no_active_reminder_set_time_first)
        } else {
            swReminder.isEnabled = true
            tvActiveReminderCaption.text =
                String.format(getString(R.string.active_reminder_caption), hour, minute)
        }

        if (reminderActive) {
            tvActiveReminderCaption.setTextColor(resources.getColor(R.color.primary_text))
        } else {
            tvActiveReminderCaption.setTextColor(resources.getColor(R.color.primary_text_disabled))
        }

        swReminder.isChecked = this.reminderActive

        swReminder.setOnCheckedChangeListener { _, isChecked ->
            reminderActive = isChecked

            if (isChecked) {
                tvActiveReminderCaption.setTextColor(resources.getColor(R.color.primary_text))
            } else {
                tvActiveReminderCaption.setTextColor(resources.getColor(R.color.primary_text_disabled))
            }
        }

        setTimePicker()
        btnClose.setOnClickListener { dismiss() }
        btnSave.setOnClickListener {
            setReminder(reminderActive)
            dismiss()
        }
    }

    private fun setReminder(enabled: Boolean) {
        reminderActive = enabled

        if (enabled) {
            NotificationHelper.cancelAlarmRTC(dialog!!.context, this.hour, this.minute)
            NotificationHelper.scheduleRepeatingRTCNotification(
                dialog!!.context,
                this.tmpHour,
                this.tmpMinute
            )
            NotificationHelper.enableBootReceiver(dialog!!.context)
            userStorage.updateSettings {
                it.apply {
                    reminderActive = true
                    reminderTimeHour = tmpHour
                    reminderTimeMinute = tmpMinute
                    reminderAdded = true
                }
            }
        } else {
            userStorage.updateSettings {
                it.apply {
                    reminderActive = false
                }
            }
            NotificationHelper.cancelAlarmRTC(dialog!!.context, this.hour, this.minute)
            NotificationHelper.disableBootReceiver(dialog!!.context)
        }
    }

    private fun setTimePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.apply {
                hour = this.hour
                minute = this.minute
            }
        }

        timePicker.setOnTimeChangedListener { timePicker, hourOfDay, minute ->
            swReminder.isEnabled = true
            swReminder.isChecked = true
            this.tmpHour = hourOfDay
            this.tmpMinute = minute
            tvActiveReminderCaption.text =
                String.format(getString(R.string.active_reminder_caption), tmpHour, tmpMinute)
        }
    }
}