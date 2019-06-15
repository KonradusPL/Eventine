package com.racjonalnytraktor.findme3.ui.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.Invitation
import com.racjonalnytraktor.findme3.ui.main.fragments.join.JoinMvp
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.invitation_item.view.*


class InvitationsAdapter(val list: ArrayList<Invitation>, val joinMvp: JoinMvp.View)
    : RecyclerView.Adapter<InvitationsAdapter.MyHolder>() {
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.invitation_item, parent, false)
        return MyHolder(view,this,joinMvp)
    }

    class MyHolder(itemView: View,val adapter: InvitationsAdapter, val joinMvp: JoinMvp.View): RecyclerView.ViewHolder(itemView) {

        fun bind(invitation: Invitation, position: Int){
            itemView.fieldGroupName.text = invitation.groupName
            itemView.fieldInvitationTitle.text = invitation.invitingPerson
            itemView.buttonConfirmInvitation.setOnClickListener {
                adapter.removeItem(layoutPosition)
                joinMvp.onInvitationClick(invitation)
            }

            Picasso.get()
                    .load(invitation.imageUri)
                    .placeholder(R.drawable.image_placeholder)
                    .resize(50,50)
                    .centerCrop()
                    .into(itemView.imageGroup)
        }

    }

    fun addItem(invitation: Invitation){
        list.add(invitation)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}