package com.sirionrazzer.diary.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class TrackItem(
    @PrimaryKey
    @Required
    var id: String,
    var templateId: String, // id of template
    var archived: Boolean,
    var name: String,
    var description: String,
    var image: Int,
    var hasTextField: Boolean, // item has optional text field
    var hasNumberField: Boolean, // item has optional number field
    var hasPictureField: Boolean, // item has optional picture field
    open var status: Boolean, // on/off status of item
    open var textField: String?, // optional text field
    open var numberField: Float?, // optional number field
    open var pictureField: String?, // optional picture field
    open var date: Long, // creation time like Calendar.getInstance().timeInMillis
    open var position: Int
) : RealmObject() {
    // constructor used by Realm
    constructor() : this(
        id = "",
        templateId = "",
        archived = false,
        name = "",
        description = "",
        image = 0,
        hasTextField = false,
        hasNumberField = false,
        hasPictureField = false,
        status = false,
        textField = "",
        numberField = 0f,
        pictureField = "",
        date = 0,
        position = 0
    )
}
