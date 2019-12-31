package com.sirionrazzer.diary.models

data class ExampleTemplate(
    var name: String,
    var resource: Int,
    var hasTextField: Boolean,
    var hasNumberField: Boolean,
    var description: String,
    var selected: Boolean
)