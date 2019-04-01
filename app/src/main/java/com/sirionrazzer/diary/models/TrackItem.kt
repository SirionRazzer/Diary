package com.sirionrazzer.diary.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

open class TrackItem (
    var id: String,

    var name: String,

    // image from drawables
    var imageResource: String,

    // optional text field
    var hasTextField: Boolean,
    var textField: String?,

    // optional number field
    var hasNumberField: Boolean,
    var numberField: Float?
) : RealmObject() {
    // constructor used by Realm
    constructor() : this("", "", "", false, "", false, 0f)
}
