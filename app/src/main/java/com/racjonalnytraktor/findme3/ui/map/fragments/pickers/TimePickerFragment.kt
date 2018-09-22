package com.racjonalnytraktor.findme3.ui.map.fragments.pickers

import android.app.Dialog
import android.app.DialogFragment
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.TimePicker
import java.util.*

class TimePickerFragment(): DialogFragment(), TimePickerDialog.OnTimeSetListener {

    var onTimeSetListener: TimePickerDialog.OnTimeSetListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
        Log.d("time date","$hourOfDay : $minute")
        onTimeSetListener?.onTimeSet(p0,hourOfDay,minute)
    }
}