package com.sirionrazzer.diary.models

import io.realm.RealmObject

open class TrackItemTemplate (
    var id: String,

    var deleted: Boolean,

    var name: String,

    var imageOn: String,
    var imageOff: String
) : RealmObject() {
    // constructor used by Realm
    constructor() : this("", false,"", "", "")
}
