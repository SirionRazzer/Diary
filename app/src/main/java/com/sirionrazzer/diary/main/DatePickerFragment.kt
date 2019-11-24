package main.java.com.sirionrazzer.diary.main

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.widget.DatePicker
import com.sirionrazzer.diary.main.MainActivity
import com.sirionrazzer.diary.util.DateUtils
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId

class DatePickerFragment() : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = (activity as MainActivity).mainViewModel.date.value!!
        val year = date.year
        val month = date.monthValue
        val day = date.dayOfMonth
        return DatePickerDialog(activity, this, year, month - 1, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        (activity as MainActivity).mainViewModel.date.value = LocalDate.of(year, month + 1, dayOfMonth)
    }
}