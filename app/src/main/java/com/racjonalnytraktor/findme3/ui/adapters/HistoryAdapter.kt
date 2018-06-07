package com.racjonalnytraktor.findme3.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.Invitation
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Typed
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.network.model.info.Info
import kotlinx.android.synthetic.main.item_history.view.*


class HistoryAdapter(val list: ArrayList<Typed>, val listener: ClickListener)
    :RecyclerView.Adapter<HistoryAdapter.MyHolder>(){

    var type = "ping"

    override fun onBindViewHolder(holder: HistoryAdapter.MyHolder, position: Int) {
        if(list[position].type == "ping")
            holder.bind1(list[position] as Ping)
        else
            holder.bind2(list[position] as Info)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return MyHolder(view)
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind1(ping: Ping) {
            itemView.setOnClickListener {
                listener.onPingClick(ping)
            }
            itemView.fieldTitle.text = ping.title
            itemView.fieldDescr.text = ping.desc
            val creator = if(ping.creatorName.isNotEmpty()) ping.creatorName else "nieznany"
            itemView.fieldCreator.text = "Autor: " + creator
        }
        fun bind2(info: Info){
            itemView.fieldTitle.text = "Informacja"
            val creator = if(info.creatorName.isNotEmpty()) info.creatorName else "nieznany"
            itemView.fieldCreator.text = "Autor: " + creator
            itemView.fieldDescr.text = info.content
        }
    }

    fun clear(type: String){
        notifyItemRangeRemoved(0,list.size)
        list.clear()
        this.type = type
    }

    fun updateList(item: Typed){
        list.add(item)
        notifyItemInserted(list.size-1)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ClickListener{
        fun onInfoClick(info: Info)
        fun onPingClick(ping: Ping)
    }
}