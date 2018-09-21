package com.racjonalnytraktor.findme3.ui.map.fragments

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.new.CreateActionRequest
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.map.MapMvp
import kotlinx.android.synthetic.main.add_task_description.*
import java.util.*

class AddTaskDescrFragment<V: MapMvp.View>: BaseFragment<V>(){

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
            parentMvp.getPresenter().onChangeLocationClick()
        }

        buttonAddUsers.setOnClickListener {
            parentListener.onAddUsersClick()
        }

    }

    fun showClockDialog() {
        val viewDate = layoutInflater.inflate(R.layout.dialog_time,null)
        val timePicker = TimePicker(parentMvp.getCtx())
        val builder = AlertDialog.Builder(parentMvp.getCtx())
                .setView(timePicker)
                .setPositiveButton("OK") { dialogInterface, i ->
                    try {
                        //date.time = timePicker.tex* 3600

                    }catch (e: Exception){
                        Log.d("asdasdads","asdssad")
                    }
                }
                .setNegativeButton("ANULUJ") { _, _ -> }
        builder.create().show()
    }

    fun getActionData(): CreateActionRequest{
        val action = CreateActionRequest()
        //action.title = fieldTitle
        return CreateActionRequest()
    }
}