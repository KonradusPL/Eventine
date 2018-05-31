package com.racjonalnytraktor.findme3.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.Person
import com.racjonalnytraktor.findme3.data.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.friend_item.view.*
import java.util.ArrayList


class FriendsAdapter(val list: ArrayList<User>,
                     val context: Context) : RecyclerView.Adapter<FriendsAdapter.MyHolder>() {

    val checkedList = ArrayList<Int>()

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.friend_item, parent, false)
        return MyHolder(view,context)
    }

    inner class MyHolder(itemView: View, val context: Context): RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int){
            itemView.fieldFriend.text = list[position].fullName
            itemView.checkFriend.setOnClickListener {
                if (itemView.checkFriend.isChecked){
                    checkedList.add(position)
                }else
                    checkedList.removeAt(checkedList.indexOf(position))
            }
            Picasso.get()
                    .load(list[position].profileUri)
                    .resize(50,50)
                    .into(itemView.imageGroup)
        }

    }

    fun getCheckedFriends(): List<String>{
        val array = ArrayList<String>()
        for(i in checkedList){
            array.add(list[i].facebookId)
        }
        return array
    }

    fun addItem(user: User){
        list.add(user)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}