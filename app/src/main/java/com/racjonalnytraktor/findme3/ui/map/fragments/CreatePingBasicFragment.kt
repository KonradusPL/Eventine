package com.racjonalnytraktor.findme3.ui.map.fragments

import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.map.MapActivity
import com.racjonalnytraktor.findme3.ui.map.MapMvp
import com.racjonalnytraktor.findme3.ui.map.MapPresenter
import kotlinx.android.synthetic.main.fragment_create_group_basic.*
import org.jetbrains.anko.doAsync

class CreatePingBasicFragment<V: MapMvp.View>: BaseFragment<V>() {

    var type = "ping"

    lateinit var mPresenter: MapPresenter<MapMvp.View>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_group_basic,container,false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonNext.setOnClickListener {
            val task = fieldTask.text.toString()
            val descr = fieldDescr.text.toString()
            Log.d("taskiiii",task)
            doAsync {
                while (true){
                    Thread.sleep(500)
                    Log.d("taskiii",task.orEmpty())
                }
            }
            mPresenter.onNextButtonClick(task,descr)
        }
    }

    fun onInfo(){
        titleBasic.text = "Tworzenie informacji"

        fieldTask.visibility = View.INVISIBLE

        val set = ConstraintSet()

        set.clone(layout);
        // The following breaks the connection.
        set.clear(R.id.fieldDescr, ConstraintSet.TOP);
        // Comment out line above and uncomment line below to make the connection.
        set.connect(R.id.fieldDescr, ConstraintSet.TOP, R.id.titleBasic, ConstraintSet.BOTTOM, 8)
        set.applyTo(layout);
    }

    fun clearData(){
        titleBasic.text = "Tworzenie pingu"
        fieldTask.text.clear()
        fieldDescr.text.clear()
        if(fieldTask.visibility == View.INVISIBLE){
            type = "ping"
            fieldTask.visibility = View.VISIBLE

            val set = ConstraintSet()

            set.clone(layout)
            // The following breaks the connection.
            set.clear(R.id.fieldDescr, ConstraintSet.TOP);
            // Comment out line above and uncomment line below to make the connection.
            set.connect(R.id.fieldDescr, ConstraintSet.TOP, R.id.fieldTask, ConstraintSet.BOTTOM, 8)
            set.applyTo(layout)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }
}