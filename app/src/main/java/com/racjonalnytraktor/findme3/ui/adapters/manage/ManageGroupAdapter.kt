package com.racjonalnytraktor.findme3.ui.adapters.manage

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.map.MapMvp
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import kotlinx.android.synthetic.main.item_job_type.view.*
import kotlinx.android.synthetic.main.item_worker.view.*

internal class ManageGroupAdapter(val jobs: ArrayList<Job>, val mvpView: MapMvp.View, val type: String = "manage")
    :ExpandableRecyclerViewAdapter<ManageGroupAdapter.JobViewHolder, ManageGroupAdapter.WorkerViewHolder>(jobs){

    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_job_type,
                parent,false)
        return JobViewHolder(view,mvpView,type)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): WorkerViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_worker,
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

            itemView.iconArrow.rotation += 180

            super.onClick(v)
        }
        fun bind(job: Job){
            itemView.apply {
                val color = if(type == "addTask") Color.BLACK else Color.WHITE
                textOrganiser.text = job.name
                textOrganiser.setTextColor(color)
                iconArrow.setImageDrawable(IconicsDrawable(mvpView.getCtx())
                        .icon(GoogleMaterial.Icon.gmd_keyboard_arrow_down)
                        .sizeDp(18)
                        .color(color))

            }
        }
    }
    internal class WorkerViewHolder(itemView: View,val mvpView: MapMvp.View, val type: String): ChildViewHolder(itemView){
        fun bind(worker: Worker){
            itemView.textWorker.text = worker.name
            if(type == "addTask"){
                itemView.textWorker.setTextColor(Color.BLACK)
                itemView.iconAddWorker.setImageDrawable(IconicsDrawable(mvpView.getCtx())
                        .icon(FontAwesome.Icon.faw_plus)
                        .sizeDp(14)
                        .color(Color.BLACK))
            }
        }
    }
}