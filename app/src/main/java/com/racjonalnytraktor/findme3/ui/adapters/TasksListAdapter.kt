package com.racjonalnytraktor.findme3.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.Task
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.task_item.view.*
import java.util.ArrayList

class TasksListAdapter(val list: ArrayList<Task>,
                     val context: Context) : RecyclerView.Adapter<TasksListAdapter.MyHolder>() {

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return MyHolder(view,context)
    }

    class MyHolder(itemView: View, val context: Context): RecyclerView.ViewHolder(itemView) {

        fun bind(task: Task){
            itemView.fieldTaskName.text = task.taskName
            itemView.fieldBoss.text = context.getString(R.string.text_commissioned_from) + " ${task.boss}"
            Picasso.get()
                    .load(task.groupImageUri)
                    .resize(50,50)
                    .into(itemView.imageGroup)
        }

    }

    fun addItem(task: Task){
        list.add(task)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}