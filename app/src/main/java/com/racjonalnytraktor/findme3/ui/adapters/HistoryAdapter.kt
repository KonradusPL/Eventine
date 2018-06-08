package com.racjonalnytraktor.findme3.ui.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.common.StringUtils
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.Invitation
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Typed
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.network.model.info.Info
import com.racjonalnytraktor.findme3.utils.StringHelper
import kotlinx.android.synthetic.main.item_history.view.*


class HistoryAdapter(val list: ArrayList<Typed>, val listener: ClickListener,val context: Context)
    :RecyclerView.Adapter<HistoryAdapter.MyHolder>(){

    var type = "ping"

    override fun onBindViewHolder(holder: HistoryAdapter.MyHolder, position: Int) {
        Log.d("metodzika",list[position].type)
        if(list[position].type == "ping")
            holder.bind1(list[position] as Ping)
        else
            holder.bind2(list[position] as Info)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
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

            if (ping.ended){
                itemView.textStatus.setText("wykonany")
                itemView.textStatus.setTextColor(ContextCompat.getColor(context,R.color.green))
            }
            else if(ping.inProgress){
                itemView.textStatus.setText("W trakcie")
                itemView.textStatus.setTextColor(ContextCompat.getColor(context,R.color.orange))
            }
            else
                itemView.textStatus.text = "nie zaczęty"

            itemView.fieldTitle.text = ping.title
            itemView.fieldDescr.text = ping.desc
            val creator = if(ping.creatorName.isNotEmpty()) ping.creatorName else "nieznany"
            itemView.fieldCreator.text = "Autor: " + creator
            if(ping.createdAt != null && ping.createdAt.isNotEmpty() && ping.date.orEmpty().isEmpty())
                itemView.fieldDate.text = "Data stworzenia: ${StringHelper.getCalendarText(ping.createdAt.orEmpty())}"
            else if (ping.date.orEmpty().isNotEmpty())
                itemView.fieldDate.text = "Zaplanowano na : ${StringHelper.getCalendarText(ping.date.orEmpty())}"

        }
        fun bind2(info: Info){
            itemView.textStatus.text = ""

            itemView.setOnClickListener {  }
            itemView.fieldTitle.text = "Informacja"
            val creator = if(info.creatorName.isNotEmpty()) info.creatorName else "nieznany"
            itemView.fieldCreator.text = "Autor: " + creator
            itemView.fieldDescr.text = info.content
            if(info.createdAt != null && info.createdAt.isNotEmpty() && info.date.orEmpty().isEmpty())
                itemView.fieldDate.text = "Data stworzenia: ${StringHelper.getCalendarText(info.createdAt.orEmpty())}"
            else if(info.date.orEmpty().isNotEmpty())
                itemView.fieldDate.text = "Zaplanowano na : ${StringHelper.getCalendarText(info.date.orEmpty())}"
        }
    }

    fun clear(type: String){
        list.clear()
        notifyDataSetChanged()
        this.type = type
    }

    fun updateList(item: Typed){
        list.add(item)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ClickListener{
        fun onInfoClick(info: Info)
        fun onPingClick(ping: Ping)
    }

    enum class HItemType{
        PING,INFO
    }
}