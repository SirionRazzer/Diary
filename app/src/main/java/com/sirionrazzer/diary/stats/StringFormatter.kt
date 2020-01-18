package com.sirionrazzer.diary.stats

import com.github.mikephil.charting.formatter.ValueFormatter

class StringFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return value.toString()
    }
}