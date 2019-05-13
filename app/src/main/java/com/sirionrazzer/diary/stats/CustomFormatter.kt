package com.sirionrazzer.diary.stats

import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.formatter.ValueFormatter

class CustomFormatter(private val chart: BarLineChartBase<*>) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {

        return value.toString()
    }
}


