package com.sirionrazzer.diary.models

data class TrackItem (
    var name: String,

    // image from drawables
    var imageResource: String,

    // optional text field
    var hasTextField: Boolean,
    var textField: String?,

    // optional number field
    var hasNumberField: Boolean,
    var numberField: Float?
)
