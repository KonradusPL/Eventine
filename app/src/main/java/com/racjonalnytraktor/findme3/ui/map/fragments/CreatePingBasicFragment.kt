package com.racjonalnytraktor.findme3.ui.map.fragments

import android.content.Context
import android.os.Bundle
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

    fun clearData(){
        fieldTask.text.clear()
        fieldDescr.text.clear()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        mPresenter = (context as MapActivity).mPresenter
    }
}