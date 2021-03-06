package com.racjonalnytraktor.findme3.ui.adapters

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.map.MapMvp
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_subgroup.view.*
import java.util.ArrayList


class SubGroupsAdapter(val list: ArrayList<String> = ArrayList(),
                       val mvpView: MapMvp.View) : RecyclerView.Adapter<SubGroupsAdapter.MyHolder>() {

    val checkedList: ArrayList<String> = ArrayList()

    inner class MyHolder(itemView: View, val view: MapMvp.View): RecyclerView.ViewHolder(itemView) {
        fun bind(group: String){

            if(checkedList.contains(group)){
                itemView.setBackgroundColor(ContextCompat.getColor(mvpView.getCtx(),R.color.colorAccent))
            }else{
                itemView.setBackgroundColor(ContextCompat.getColor(mvpView.getCtx(),R.color.backgroundColor))

            }


            itemView.fieldGroupName.text = group
            itemView.setOnClickListener {
                if(checkedList.contains(group)){
                    checkedList.remove(group)
                    itemView.setBackgroundColor(ContextCompat.getColor(mvpView.getCtx(),R.color.backgroundColor))
                }else{
                    checkedList.add(group)
                    itemView.setBackgroundColor(ContextCompat.getColor(mvpView.getCtx(),R.color.colorAccent))

                }
            }
            if(checkedList.contains(group)){
                itemView.setBackgroundColor(ContextCompat.getColor(mvpView.getCtx(),R.color.colorAccent))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_subgroup, parent, false)
        return MyHolder(view, mvpView)
    }

    fun updateList(list: List<String>){
        Log.d("list",list.size.toString())
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun clear(){
        list.clear()
        checkedList.clear()
        notifyDataSetChanged()
    }

    fun updatelist(item: String){
        list.add(item)
        notifyItemInserted(list.size)
    }

    fun updateCheckedGroups(list: List<String>){
        checkedList.addAll(list)
        notifyDataSetChanged()
    }

    fun getCheckedGroups(): ArrayList<String>{
        return checkedList
    }

    override fun getItemCount(): Int {
        Log.d("asdasdzzzz","aaaaaa")
        return list.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(list[position])
    }
}