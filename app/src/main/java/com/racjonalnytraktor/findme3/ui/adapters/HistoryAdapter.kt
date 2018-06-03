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
        return MyHolder(view, this)
    }

    class MyHolder(itemView: View, val adapter: HistoryAdapter) : RecyclerView.ViewHolder(itemView) {

        fun bind1(ping: Ping) {
            itemView.fieldTitle.text = ping.title
            itemView.fieldDescr.text = ping.desc
            itemView.fieldCreator.text = ping.desc
        }
        fun bind2(info: Info){
            itemView.fieldCreator.text = info.creatorName
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
        notifyDataSetChanged()
    }

    fun updateInfo(item: Info){
        infos.add(item)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        if (type == "pings")
            return pings.size
        else
            return infos.size
    }
}