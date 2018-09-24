package com.racjonalnytraktor.findme3.ui.map.fragments.addtask

import android.app.Activity
import android.app.TimePickerDialog
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
import java.util.*

class AddTaskDescrFragment<V: MapMvp.View>: BaseFragment<V>(), TimePickerDialog.OnTimeSetListener
,Listener.ChangeLocation{

    lateinit var parentListener: DescriptionListener

    var date = Date()

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

    fun showClockDialog() {
        val viewDate = layoutInflater.inflate(R.layout.dialog_time,null)
        //val timePicker = TimePicker(parentMvp.getCtx())
        val timePicker = TimePickerFragment()
        timePicker.show((parentContext as Activity).fragmentManager,"")

    }

    fun getActionData(): CreateActionRequest{
        val action = CreateActionRequest()
        action.title = fieldTitle?.text.toString()
        action.descr = fieldTitle?.text.toString()
        action.plannedTime = date

        return CreateActionRequest()
    }

    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
        date.time = (hourOfDay * 3600 + minute * 60).toLong()
    }

    override fun changeLocation(text: String){
        textLocation?.text = text
    }

}