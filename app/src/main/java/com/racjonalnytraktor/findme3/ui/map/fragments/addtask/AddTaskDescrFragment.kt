package com.racjonalnytraktor.findme3.ui.map.fragments.addtask

import android.app.Activity
import android.app.TimePickerDialog
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.new.CreateActionRequest
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.map.MapMvp
import com.racjonalnytraktor.findme3.ui.map.fragments.pickers.TimePickerFragment
import com.racjonalnytraktor.findme3.ui.map.listeners.Listener
import kotlinx.android.synthetic.main.add_task_description.*
import kotlinx.android.synthetic.main.fragment_add_task.*
import java.util.*

class AddTaskDescrFragment<V: MapMvp.View>: BaseFragment<V>(), TimePickerDialog.OnTimeSetListener
,Listener.ChangeLocation{

    lateinit var parentListener: DescriptionListener

    var date = Date()

    var mLocation: Location = Location("")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_task_description,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fieldTitle.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0 != null)
                    parentListener.onTitleChanged(p0.toString())
            }

        })

        buttonShowDate.setOnClickListener {
            showClockDialog()
        }

        buttonChangeLocation.setOnClickListener {
            parentMvp.getPresenter().onChangeLocationClick(this)
        }

        buttonAddUsers.setOnClickListener {
            parentListener.onAddUsersClick()
        }

    }

    private fun showClockDialog() {
        val viewDate = layoutInflater.inflate(R.layout.dialog_time,null)
        //val timePicker = TimePicker(parentMvp.getCtx())
        val timePicker = TimePickerFragment()
        timePicker.onTimeSetListener = this
        timePicker.show((parentContext as Activity).fragmentManager,"")

    }

    fun getActionData(): CreateActionRequest{
        val action = CreateActionRequest()
        action.title = fieldTitle?.text.toString()
        action.descr = fieldDescr?.text.toString()
        action.plannedTime = date
        try {
            action.geo[0] = mLocation.latitude
            action.geo[1] = mLocation.longitude

        }catch (e: ArrayIndexOutOfBoundsException){

        }

        return action
    }

    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
        date.time = (hourOfDay * 3600 + minute * 60).toLong()
        val hourText = if(hourOfDay < 10) "0$hourOfDay" else "$hourOfDay"
        val minuteText = if(minute < 10) "0$minute" else "$minute"
        parentListener.onDateChanged("Termin powiadomienia: $hourOfDay:$minuteText")
    }

    override fun changeLocation(location: Location){
        mLocation.set(location)
        textLocation?.text = "${location.latitude.toFloat()}, ${location.longitude.toFloat()}"
    }

}