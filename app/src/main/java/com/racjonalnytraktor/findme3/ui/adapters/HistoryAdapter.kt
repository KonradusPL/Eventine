package com.racjonalnytraktor.findme3.ui.adapters

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.Model1
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.network.model.info.Info
import com.racjonalnytraktor.findme3.ui.map.MapMvp
import kotlinx.android.synthetic.main.item_history_new.view.*


class HistoryAdapter(var list: ArrayList<Model1>,val mvpView: MapMvp.View)
    : RecyclerView.Adapter<HistoryAdapter.MyHolder>(){

    private val statusIcons = ArrayList<IIcon>()

    init {
        statusIcons.add(FontAwesome.Icon.faw_hourglass_start)
        statusIcons.add(FontAwesome.Icon.faw_hourglass_half)
        statusIcons.add(FontAwesome.Icon.faw_hourglass_end)
    }

    var type = "ping"

    override fun onBindViewHolder(holder: HistoryAdapter.MyHolder, position: Int) {
        holder.bind1(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history_new, parent, false)
        return MyHolder(view)
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind1(model1: Model1) {
            itemView.apply {
                fieldTitle.text = model1.title
                fieldMessage.text = model1.message
                fieldDate.text = model1.date
                if(type == "ping"){
                    val icon = when(model1.status){
                        "inProgress" -> statusIcons[1]
                        "done" -> statusIcons[2]
                        else -> statusIcons[0]
                    }

                    iconStatus.setImageDrawable(IconicsDrawable(mvpView.getCtx())
                            .icon(icon)
                            .sizeDp(14)
                            .color(ContextCompat.getColor(mvpView.getCtx(),R.color.colorPrimary)))
                }else{
                    iconStatus.setImageDrawable(null)
                }
                setOnClickListener {
                    val ping = Ping()
                    ping.title = model1.title
                    ping.desc = model1.message
                    ping.pingId = model1.id
                    if(model1.status == "inProgress")
                        ping.inProgress = true
                    else if(model1.status == "done")
                        ping.ended = true
                    if(type == "ping")
                        mvpView.showEndPingBar(ping)
                }
            }
        }
    }

    fun clear(type: String){
        list.clear()
        notifyDataSetChanged()
        this.type = type
    }

    fun updateList(item: Model1){
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

    enum class ItemType{
        INFO,HELP
    }
}