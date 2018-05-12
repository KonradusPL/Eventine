package com.racjonalnytraktor.findme3.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.Person
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.friend_item.view.*
import java.util.ArrayList


class FriendsAdapter(val list: ArrayList<Person>,
                     val context: Context) : RecyclerView.Adapter<FriendsAdapter.MyHolder>() {

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.friend_item, parent, false)
        return MyHolder(view,context)
    }

    class MyHolder(itemView: View, val context: Context): RecyclerView.ViewHolder(itemView) {

        fun bind(person: Person){
            itemView.fieldFriend.text = person.fullName
            Picasso.get()
                    .load(person.profileImageUri)
                    .resize(50,50)
                    .into(itemView.imageGroup)
        }

    }

    fun addItem(person: Person){
        list.add(person)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}