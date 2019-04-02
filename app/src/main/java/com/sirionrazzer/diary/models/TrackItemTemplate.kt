package com.sirionrazzer.diary.models

import io.realm.RealmObject

open class TrackItemTemplate (
    var id: String,

    var deleted: Boolean,

    var name: String,

    var imageOn: String,
    var imageOff: String,

    var hasTextField: Boolean,
    var hasNumberField: Boolean
) : RealmObject() {
    // constructor used by Realm
    constructor() : this("", false,"", "", "", false, false)
}
