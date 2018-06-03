package com.racjonalnytraktor.findme3.ui.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Header
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Typed
import com.racjonalnytraktor.findme3.data.network.model.changegroups.UserInSubGroup
import com.racjonalnytraktor.findme3.ui.map.fragments.manage.SwipeHelper
import kotlinx.android.synthetic.main.item_header.view.*
import kotlinx.android.synthetic.main.item_user_subgroup.view.*

class ManageAdapter(val list: ArrayList<Typed>, val listener: Listener)
    :RecyclerView.Adapter<ManageAdapter.MyViewHolder>()
, SwipeHelper.ActionCompletionContract{


    override fun onViewMoved(oldPosition: Int, newPosition: Int) {
        Log.d("newPosition",oldPosition.toString())
        val typed = list.get(oldPosition)
        list.removeAt(oldPosition)
        list.add(newPosition,typed)
        notifyItemMoved(oldPosition,newPosition)

        //notifyItemRemoved(oldPosition)
        //notifyItemInserted(newPosition)
    }

    override fun onViewSwiped(position: Int) {
        Log.d("onViewSwiped",position.toString())
    }


    companion object {
        const val TYPE_USER = 0
        const val TYPE_HEADER = 1
    }

    fun update(item: Typed){
        list.add(item)
        notifyItemInserted(list.size-1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return if(viewType == TYPE_USER)
            MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user_subgroup, parent, false),listener)
        else
            MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false),listener)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemViewType(position: Int): Int {
        val type = if(list[position].type == "person") TYPE_USER else TYPE_HEADER
        return type
    }

    inner class MyViewHolder(itemView: View, listener: Listener): RecyclerView.ViewHolder(itemView){
        var type = "person"
        fun bind(typed: Typed){
            if(typed.type == "person"){
                val user = typed as UserInSubGroup
                itemView.fieldName.setText(user.name)
            }else if(typed.type == "header"){
                type = "header"
                val header = typed as Header
                itemView.textTitle.setText(header.group)

            }
        }
    }

    interface Listener{

    }
}