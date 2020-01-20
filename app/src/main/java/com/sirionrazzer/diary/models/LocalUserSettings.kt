package com.sirionrazzer.diary.models

class LocalUserSettings {
    var email: String? = null

    // status
    var accountCreated: Boolean = false
    var isNewcomer: Boolean = false
    var firstTime: Boolean = true
    var boardingPickerShown: Boolean = false

    // preferences
    var reminderAdded: Boolean = false
    var reminderActive: Boolean = false
    var reminderTimeHour: Int = 12
    var reminderTimeMinute: Int = 0
}