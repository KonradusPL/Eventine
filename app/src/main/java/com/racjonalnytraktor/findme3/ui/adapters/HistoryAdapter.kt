package com.racjonalnytraktor.findme3.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.Invitation
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.network.model.info.Info
import kotlinx.android.synthetic.main.item_history.view.*


class HistoryAdapter(val pings: ArrayList<Ping>, val infos: ArrayList<Info>)
    :RecyclerView.Adapter<HistoryAdapter.MyHolder>(){

    var type = "pings"

    override fun onBindViewHolder(holder: HistoryAdapter.MyHolder, position: Int) {
        if(type == "pings")
            holder.bind1(pings[position])
        else
            holder.bind2(infos[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return MyHolder(view)
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind1(ping: Ping) {
            itemView.fieldTitle.text = ping.title
            itemView.fieldDescr.text = ping.desc
            val creator = if(ping.creatorName.isNotEmpty()) ping.creatorName else "nieznany"
            itemView.fieldCreator.text = "Autor: " + creator
        }
        fun bind2(info: Info){
            itemView.fieldTitle.text = "Info"
            val creator = if(info.creatorName.isNotEmpty()) info.creatorName else "nieznany"
            itemView.fieldCreator.text = "Autor: " + creator
            itemView.fieldDescr.text = info.content
        }
    }

    fun clear(type: String){
        pings.clear()
        infos.clear()
        notifyDataSetChanged()
        this.type = type
    }

    fun updatePings(item: Ping){
        pings.add(item)
        notifyItemInserted(pings.size-1)
    }

    fun updateInfo(item: Info){
        infos.add(item)
        notifyItemInserted(infos.size-1)
    }

    override fun getItemCount(): Int {
        if (type == "pings")
            return pings.size
        else
            return infos.size
    }
}