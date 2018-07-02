package com.racjonalnytraktor.findme3.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.Person
import kotlinx.android.synthetic.main.item_user_in_group.view.*

class UsersInGroupAdapter(val users: ArrayList<Person>): RecyclerView.Adapter<UsersInGroupAdapter.UserHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_in_group,parent,false)
        return UserHolder(view)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bind(position)
    }

    inner class UserHolder(val view: View): RecyclerView.ViewHolder(view){
        fun bind(position: Int){
            view.fieldUserName.text = users[position].fullName
        }
    }
}