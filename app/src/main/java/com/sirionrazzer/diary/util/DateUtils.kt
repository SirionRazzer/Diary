package com.sirionrazzer.diary.util

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    companion object Factory {

        /**
         * Converts from Date to Long date representation
         *
         * @param date to be converted
         * @return date represented as Long value
         */
        fun persistDate(date: Date?): Long? {
            return date?.time
        }

        /**
         * Converts from Long to Date date representation
         *
         * @param millis to be converted
         * @return date
         */
        fun dateFromMillis(millis: Long?): Date? {
            return if (millis != null) {
                Date(millis)
            } else null
        }

        /**
         * Convert date in case note was taken recently or today
         *
         * @param editDate is original edit date
         * @param specialText is true if special string events (Today, Recently) should be used
         * @return appropriate text (ie. "Recently" ot "Today") or HH:mm DD/mm for older notes
         */
        fun smartDate(editDate: Date?, specialText: Boolean): String {
            var smartDate: String
            val currentDate = Date()
            val df = SimpleDateFormat("d/M/YY")
            val cal = Calendar.getInstance()
            cal.time = currentDate

            smartDate = df.format(editDate)

            // in case of time zone change use just plain time
            if (currentDate.before(editDate)) {
                return smartDate
            }

            if (specialText) {
                // last hour (now - 1 hour) is Recently
                cal.add(Calendar.HOUR, -1)
                if (cal.time.compareTo(editDate) <= 0) {
                    smartDate = "Recently"
                    return smartDate
                }

                // last day (now - 24 hours) is Today
                cal.add(Calendar.HOUR, -23) // we already took -1 hour, so -23
                if (cal.time.compareTo(editDate) <= 0) {
                    smartDate = "Today"
                    return smartDate
                }
            }

            return smartDate
        }

        /**
         * Sunday = 0, Monday = 1, ..., Saturday = 6
         */
        fun dayInWeek(): Int {
            return Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        }

        fun getWeekAndYearPair(dateInMilliseconds: Long): Pair<Int, Int> {
            return dateFromMillis(dateInMilliseconds)?.let { getWeekAndYearPair(it) }!!
        }

        fun getMonthAndYearPair(dateInMilliseconds: Long): Pair<Int, Int> {
            return dateFromMillis(dateInMilliseconds)?.let { getMonthAndYearPair(it) }!!
        }

        fun getWeekAndYearPair(date: Date): Pair<Int, Int> {

            val cal = Calendar.getInstance()
            cal.time = date
            return Pair(cal.get(Calendar.WEEK_OF_YEAR), cal.get(Calendar.YEAR))
        }

        fun getWeekYearString(weekYear: Pair<Int, Int>): String {
            return formatAsString(weekYear.first, weekYear.second)
        }

        fun getWeekYearString(date: Long): String {
            val pair = getWeekAndYearPair(date)
            return formatAsString(pair.first, pair.second)
        }

        fun getMonthYearString(date: Long): String {
            val pair = getMonthAndYearPair(date)
            return formatAsString(pair.first, pair.second)
        }

        fun getMonthAndYearPair(date: Date): Pair<Int, Int> {
            val cal = Calendar.getInstance()
            cal.time = date
            return Pair(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR))
        }

        private fun formatAsString(x: Int, y: Int): String {
            return "$x-$y"
        }

        fun getMonthYearString(pair: Pair<Int, Int>): String {
            return formatAsString(pair.first, pair.second)
        }
    }

}
