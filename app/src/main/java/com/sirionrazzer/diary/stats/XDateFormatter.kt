package com.sirionrazzer.diary.stats

import android.content.Context
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.formatter.ValueFormatter
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.util.DateUtils
import java.util.*

class XDateFormatter(
    private val chart: BarLineChartBase<*>,
    private val barData: MutableList<Pair<Float, String>>,
    private val timeUnit: Int,
    private val context: Context
) :
    ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val pair = barData[value.toInt()]
        val cal = Calendar.getInstance()
        var pairNow: Pair<Int, Int> = Pair(0, 0)
        var pairHash = ""
        if (timeUnit == R.string.week) {
            pairNow = DateUtils.getWeekAndYearPair(cal.time)
            pairHash = DateUtils.getWeekYearString(pairNow)
        } else if (timeUnit == R.string.month) {
            pairNow = DateUtils.getMonthAndYearPair(cal.time)
            pairHash = DateUtils.getMonthYearString(pairNow)
        }
        if (pairHash == pair.second) {
            return "${context.getString(R.string.this_time_unit)} ${context.getString(timeUnit)}"
        } else if ("${pairNow.first - 1}-${pairNow.second}" == pair.second) {
            return "${context.getString(R.string.last_time_unit)} ${context.getString(timeUnit)}"
        }

        return pair.second

    }
}