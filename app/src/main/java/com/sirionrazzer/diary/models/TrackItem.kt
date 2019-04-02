package com.sirionrazzer.diary.models

open class TrackItem (
    override var id: String,
    override var deleted: Boolean,
    override var name: String,
    override var imageOn: Int,
    override var imageOff: Int,
    override var hasTextField: Boolean,
    override var hasNumberField: Boolean,
    open var status: Boolean, // on/off status of item
    open var textField: String?, // optional text field
    open var numberField: Float?, // optional number field
    open var date: Long // creation time like Calendar.getInstance().timeInMillis
) : TrackItemTemplate(id, deleted, name, imageOn, imageOff, hasTextField, hasNumberField) {
    // constructor used by Realm
    constructor() : this("",
        false,
        "",
        0,
        0,
        false,
        false,
        false,
        "",
        0f,
        0
        )
}
