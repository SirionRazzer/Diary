package com.sirionrazzer.diary.models

import io.realm.RealmObject

open class TrackItemTemplate (
    open var id: String,
    open var deleted: Boolean,
    open var name: String,
    open var imageOn: Int,
    open var imageOff: Int,
    open var hasTextField: Boolean,
    open var hasNumberField: Boolean
) : RealmObject() {
    // constructor used by Realm
    constructor() : this("", false,"", 0, 0, false, false)
}
