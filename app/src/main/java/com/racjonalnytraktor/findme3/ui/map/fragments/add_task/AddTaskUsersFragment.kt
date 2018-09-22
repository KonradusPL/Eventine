package com.racjonalnytraktor.findme3.ui.map.fragments.add_task

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.adapters.manage.Job
import com.racjonalnytraktor.findme3.ui.adapters.manage.ManageGroupAdapter
import com.racjonalnytraktor.findme3.ui.adapters.manage.Worker
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.map.MapMvp
import kotlinx.android.synthetic.main.add_task_users.*

class AddTaskUsersFragment<V: MapMvp.View>: BaseFragment<V>() {

    private lateinit var mListAdapter: ManageGroupAdapter
    lateinit var parentListener: UsersListener


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_task_users,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iconBack.setImageDrawable(IconicsDrawable(parentMvp.getCtx())
                .sizeDp(18)
                .color(Color.BLACK)
                .icon(GoogleMaterial.Icon.gmd_arrow_back))
        iconBack.setOnClickListener {
            parentListener.onBackArrowPressed()
        }
        showList(emptyList())
    }

    fun showList(list: List<Job>) {
        val workers = ArrayList<Worker>()
        val workers1 = ArrayList<Worker>()
        val jobs = ArrayList<Job>()

        workers.add(Worker("Jan Kowalski",""))
        workers.add(Worker("Mateusz Zawada",""))
        workers.add(Worker("John Doe",""))
        workers.add(Worker("Ewelina Nowak",""))
        workers.add(Worker("Ryszard Mularski",""))
        workers.add(Worker("Martyna Kawa",""))

        workers1.add(Worker("Jan Kowalski",""))
        workers1.add(Worker("Mateusz Zawada",""))
        workers1.add(Worker("John Doe",""))
        workers1.add(Worker("Ewelina Nowak",""))
        workers1.add(Worker("Ryszard Mularski",""))
        workers1.add(Worker("Martyna Kawa",""))

        val stringArray = arrayListOf("Organizator","MC","Logistyka","Marketing & PR","Sprzedaż","Serwis")

        jobs.add(Job(stringArray[0],6,workers))
        jobs.add(Job(stringArray[1],6,workers1))

        val arrayList = ArrayList<Job>()
        arrayList.addAll(list)
        mListAdapter = ManageGroupAdapter(jobs,parentMvp,"addTask")

        listGroups.layoutManager = LinearLayoutManager(parentMvp.getCtx())
        listGroups.adapter = mListAdapter
    }

    fun getList(): ArrayList<String>{
        return mListAdapter.getSelectedWorkers()
    }

}