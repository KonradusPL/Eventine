package com.racjonalnytraktor.findme3.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.Group
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.group_item.view.*
import java.util.ArrayList

class GroupsListAdapter(val list: ArrayList<Group>,
                        val context: Context) : RecyclerView.Adapter<GroupsListAdapter.MyHolder>() {

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.group_item, parent, false)
        return MyHolder(view,context)
    }

    class MyHolder(itemView: View, val context: Context): RecyclerView.ViewHolder(itemView) {

        fun bind(group: Group){
            itemView.fieldGroupName.text = group.groupName
            Picasso.get()
                    .load(group.groupPictureUri)
                    .placeholder(R.drawable.image_placeholder)
                    .resize(50,50)
                    .into(itemView.imageGroup)
        }

    }

    fun addItem(group: Group){
        list.add(group)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}