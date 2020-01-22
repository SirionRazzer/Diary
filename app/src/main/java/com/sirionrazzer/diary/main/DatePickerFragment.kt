package com.sirionrazzer.diary.main

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.widget.DatePicker
import org.threeten.bp.LocalDate

class DatePickerFragment() : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = (activity as MainActivity).mainViewModel.date.value!!
        val year = date.year
        val month = date.monthValue
        val day = date.dayOfMonth
        return DatePickerDialog(activity, this, year, month - 1, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        (activity as MainActivity).mainViewModel.setForDate(LocalDate.of(year, month + 1, dayOfMonth))
    }
}