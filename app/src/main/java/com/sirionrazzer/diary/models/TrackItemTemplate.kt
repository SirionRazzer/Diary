package com.sirionrazzer.diary.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class TrackItemTemplate(
    @PrimaryKey
    @Required
    open var id: String,
    open var archived: Boolean,
    open var selected: Boolean,
    open var name: String,
    open var description: String,
    open var image: Int,
    open var hasTextField: Boolean,
    open var hasNumberField: Boolean,
    open var hasPictureField: Boolean,
    open var position: Int // relative position in layout, for displaying items in some order
) : RealmObject() {
    // constructor used by Realm
    constructor() : this(
        id = "",
        archived = false,
        selected = false,
        name = "",
        description = "",
        image = 0,
        hasTextField = false,
        hasNumberField = false,
        hasPictureField = false,
        position = 0
    )
}
