package com.yrdtechnologies.utility

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import java.util.Calendar

/**
 * Created by kartikshah on 17/02/18.
 */
class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    var button: Button? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(activity, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        button!!.text = "$day/$month/$year"
    }
}