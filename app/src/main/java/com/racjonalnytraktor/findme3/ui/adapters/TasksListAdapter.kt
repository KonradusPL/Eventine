package com.racjonalnytraktor.findme3.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.Task
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.utils.StringHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_history.view.*
import kotlinx.android.synthetic.main.task_item.view.*
import java.util.ArrayList

class TasksListAdapter(val list: ArrayList<Ping>,
                     val context: Context) : RecyclerView.Adapter<TasksListAdapter.MyHolder>() {

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return MyHolder(view,context)
    }

    class MyHolder(itemView: View, val context: Context): RecyclerView.ViewHolder(itemView) {

        fun bind(task: Ping){
            itemView.fieldCreator.text = task.creatorName
            itemView.fieldTitle.text = task.title
            itemView.fieldDescr.text = task.desc

            if(task.createdAt != null && task.createdAt.isNotEmpty())
                itemView.fieldDate.text = "Data stworzenia: ${StringHelper.getCalendarText(task.createdAt.orEmpty())}"
            else
                itemView.fieldDate.text = "Zaplanowano na : ${StringHelper.getCalendarText(task.date.orEmpty())}"

        }

    }

    fun addItem(ping: Ping){
        list.add(ping)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}