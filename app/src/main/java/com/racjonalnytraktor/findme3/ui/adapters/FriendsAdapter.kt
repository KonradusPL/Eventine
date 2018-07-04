package com.racjonalnytraktor.findme3.ui.adapters

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
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
        Log.d("zxczxczxc","zxczxczxc")
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.friend_item, parent, false)
        return MyHolder(view,context)
    }

    inner class MyHolder(itemView: View, val context: Context): RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int){

            if(!checkedList.contains(position)){
                itemView.setBackgroundColor(Color.WHITE)
                itemView.checkFriend.isChecked = false
            }else{
                itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.lightGrey))
                itemView.checkFriend.isChecked = true
            }

            itemView.setOnClickListener {
                Log.d("popopo",position.toString())
                if (itemView.checkFriend.isChecked){
                    itemView.setBackgroundColor(Color.WHITE)
                    checkedList.remove(position)
                    itemView.checkFriend.isChecked = false
                }
                else{
                    itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.lightGrey))
                    checkedList.add(position)
                    itemView.checkFriend.isChecked = true
                }
            }
            itemView.fieldFriend.text = list[position].fullName
            itemView.checkFriend.setOnClickListener {
                if (itemView.checkFriend.isChecked){
                    checkedList.add(position)
                    itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.lightGrey))
                }else{
                    itemView.setBackgroundColor(Color.WHITE)
                    checkedList.remove(position)
                }
            }
            /*if (list[position].facebookId.isNotEmpty() && list[position].profileUri.isNotEmpty())
                Picasso.get()
                    .load(list[position].profileUri)
                    .resize(50,50)
                    .into(itemView.imageGroup)
            else
                itemView.imageGroup.setImageResource(R.drawable.user_holder)*/
        }

    }

    fun clearFriends(){
        list.clear()
        checkedList.clear()
        notifyDataSetChanged()
    }

    fun unCheckFriends(){
        checkedList.clear()
        notifyDataSetChanged()
    }

    fun getCheckedFriends(): List<User>{
        val array = ArrayList<User>()
        for(i in checkedList){
            array.add(list[i])
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