package com.racjonalnytraktor.findme3.ui.adapters.manage

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.map.MapMvp
import com.squareup.picasso.Picasso
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import kotlinx.android.synthetic.main.item_job_type.view.*
import kotlinx.android.synthetic.main.item_user_with_profile.view.*
import kotlinx.android.synthetic.main.item_worker.view.*

internal class ManageGroupAdapter(val jobs: ArrayList<Job>, val mvpView: MapMvp.View, val type: String = "manage")
    :ExpandableRecyclerViewAdapter<ManageGroupAdapter.JobViewHolder, ManageGroupAdapter.WorkerViewHolder>(jobs){

    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_job_type,
                parent,false)
        return JobViewHolder(view,mvpView,type)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): WorkerViewHolder {
        val itemId = if(type == "organizer") R.layout.item_user_with_profile
                    else R.layout.item_worker
        val view = LayoutInflater.from(parent!!.context).inflate(itemId,
                parent,false)
        return WorkerViewHolder(view,mvpView,type)
    }

    override fun onBindChildViewHolder(holder: WorkerViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?, childIndex: Int) {
        val worker = (group as Job).workers[childIndex]
        holder?.bind(worker)
    }

    override fun onBindGroupViewHolder(holder: JobViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?) {
        holder?.bind(group as Job)
    }

    internal class JobViewHolder(itemView: View,val  mvpView: MapMvp.View, val type: String): GroupViewHolder(itemView){
        override fun onClick(v: View?) {
            if (type == "manage")
                itemView.iconArrow.rotation += 180
            else
                itemView.iconArrow2.rotation += 180

            super.onClick(v)
        }
        fun bind(job: Job){
            itemView.apply {
                val color = if(type == "addTask" || type == "organizer") Color.BLACK else Color.WHITE
                textOrganiser.text = job.name
                textOrganiser.setTextColor(color)
                fieldUserCount.text = job.workersCount.toString()
                if(type == "manage"){
                    iconArrow.setImageDrawable(IconicsDrawable(mvpView.getCtx())
                            .icon(GoogleMaterial.Icon.gmd_keyboard_arrow_down)
                            .sizeDp(18)
                            .color(color))
                }else{
                    iconArrow2.visibility = View.VISIBLE
                    iconArrow2.setImageDrawable(IconicsDrawable(mvpView.getCtx())
                            .icon(GoogleMaterial.Icon.gmd_keyboard_arrow_down)
                            .sizeDp(18)
                            .color(color))
                }
            }
        }
    }

    fun getSelectedWorkers(): ArrayList<String>{
        val workersList = ArrayList<String>()
        for(job in jobs){
            for(worker in job.workers){
                if(worker.selected)
                    workersList.add(worker.id)
            }
        }
        return workersList
    }
    internal class WorkerViewHolder(itemView: View,val mvpView: MapMvp.View, val type: String): ChildViewHolder(itemView){

        fun bind(worker: Worker){
            if(type == "organizer"){
                itemView.apply {
                    textFullName.text = worker.name
                    if (worker.profileUrl.isNotEmpty())
                        Picasso.get()
                            .load(worker.profileUrl)
                            .resize(50,50)
                            .into(imageProfile)
                }
                val location = if(worker.location != "") worker.location else "nieznane"
                itemView.textLocation.text = "Położenie: $location"
                itemView.setOnClickListener {
                    mvpView.getPresenter().onOrganizerClick(worker.id)
                }
            }
            else
                itemView.textWorker.text = worker.name
            if(type == "addTask"){
                changeOnSelected(itemView,worker.selected)

                itemView.apply {
                    iconAddWorker.setOnClickListener {
                        worker.selected = !worker.selected
                        changeOnSelected(itemView,worker.selected)
                    }
                    setOnClickListener {
                        worker.selected = !worker.selected
                        changeOnSelected(itemView,worker.selected)
                    }
                }
            }
        }
        private fun changeOnSelected(view: View, selected: Boolean){
            val icon = if(!selected) FontAwesome.Icon.faw_plus
                        else FontAwesome.Icon.faw_times
            val color = if(!selected) Color.BLACK
                    else ContextCompat.getColor(mvpView.getCtx(),R.color.colorPrimaryNew)

            view.iconAddWorker.setImageDrawable(IconicsDrawable(mvpView.getCtx())
                    .icon(icon)
                    .sizeDp(14)
                    .color(color))
            view.textWorker.setTextColor(color)

        }

    }
}