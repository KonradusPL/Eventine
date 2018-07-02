package com.racjonalnytraktor.findme3.ui.adapters

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.Group
import com.racjonalnytraktor.findme3.data.model.GroupWithUsers
import com.racjonalnytraktor.findme3.ui.main.fragments.feed.FeedMvp
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_groups.view.*
import java.util.ArrayList

class GroupsListAdapter(val list: ArrayList<GroupWithUsers>,
                        val mvpView: FeedMvp.View) : RecyclerView.Adapter<GroupsListAdapter.MyHolder>() {

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_groups, parent, false)
        return MyHolder(view,mvpView)
    }

    class MyHolder(itemView: View, val view: FeedMvp.View): RecyclerView.ViewHolder(itemView) {

        private val listener = view as GroupsListListener

        fun bind(group: GroupWithUsers){
            itemView.fieldTitle.text = group.group.groupName

            /*Picasso.get()
                    .load(group.groupPictureUri)
                    .placeholder(R.drawable.image_placeholder)
                    .resize(50,50)
                    .into(itemView.imageGroup)*/

            itemView.setOnClickListener {
                listener.onGroupsItemClick(group.group.groupName,group.group.id)

                itemView.listPeopleInGroup.apply {
                    visibility = if(visibility == View.GONE)
                        View.VISIBLE
                    else
                        View.GONE
                }

            }
            itemView.listPeopleInGroup.apply {
                layoutManager = LinearLayoutManager(view.getCtx())
                adapter = UsersInGroupAdapter(group.users)
            }
        }

    }

    fun addItem(group: GroupWithUsers){
        list.add(group)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface GroupsListListener{
        fun onGroupsItemClick(groupName: String,groupId: String)
    }
}