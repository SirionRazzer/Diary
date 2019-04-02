package com.sirionrazzer.diary.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

open class TrackItem (
    var id: String,

    // TrackItem assigned template
    var templateId: String,

    // on/off status of item
    var status: Boolean,

    // optional text field
    var hasTextField: Boolean,
    var textField: String?,

    // optional number field
    var hasNumberField: Boolean,
    var numberField: Float?,

    var date: Long // Calendar.getInstance().timeInMillis
) : RealmObject() {
    // constructor used by Realm
    constructor() : this("", "", false, false, "", false, 0f, 0)
}
