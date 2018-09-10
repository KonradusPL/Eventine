package com.racjonalnytraktor.findme3.ui.adapters.manage

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

internal class ManageGroupAdapter(val jobs: ArrayList<Job>, val mvpView: MapMvp.View)
    :ExpandableRecyclerViewAdapter<ManageGroupAdapter.JobViewHolder, ManageGroupAdapter.WorkerViewHolder>(jobs){

    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_job_type,
                parent,false)
        return JobViewHolder(view,mvpView)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): WorkerViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_worker,
                parent,false)
        return WorkerViewHolder(view,mvpView)
    }

    override fun onBindChildViewHolder(holder: WorkerViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?, childIndex: Int) {
        val worker = (group as Job).workers[childIndex]
        holder?.bind(worker)
    }

    override fun onBindGroupViewHolder(holder: JobViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?) {
        holder?.bind(group as Job)
    }

    internal class JobViewHolder(itemView: View,val  mvpView: MapMvp.View): GroupViewHolder(itemView){
        fun bind(job: Job){
            itemView.apply {
                textOrganiser.text = job.name
                iconArrow.setImageDrawable(IconicsDrawable(mvpView.getCtx())
                        .icon(GoogleMaterial.Icon.gmd_keyboard_arrow_down)
                        .sizeDp(18)
                        .color(Color.WHITE))
            }
        }
    }
    internal class WorkerViewHolder(itemView: View,val mvpView: MapMvp.View): ChildViewHolder(itemView){
        fun bind(worker: Worker){
            itemView.textWorker.text = worker.name
        }
    }
}