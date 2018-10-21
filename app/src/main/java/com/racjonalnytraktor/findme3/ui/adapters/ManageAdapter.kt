package com.racjonalnytraktor.findme3.ui.adapters

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Header
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Typed
import com.racjonalnytraktor.findme3.data.network.model.changegroups.UserInSubGroup
import com.racjonalnytraktor.findme3.ui.manage.ManageMvp
import com.racjonalnytraktor.findme3.ui.map.fragments.manage.SwipeHelper
import kotlinx.android.synthetic.main.item_header.view.*
import kotlinx.android.synthetic.main.item_user_subgroup.view.*

class ManageAdapter(val list: ArrayList<Typed>, val mvpView: ManageMvp.View, val touchHelper: ItemTouchHelper)
    :RecyclerView.Adapter<ManageAdapter.MyViewHolder>()
, SwipeHelper.ActionCompletionContract{


    override fun onViewMoved(oldPosition: Int, newPosition: Int) {

        if(list[newPosition].type == "header"){
            var newGroup = ""
            if(newPosition < oldPosition){
                for (i in newPosition downTo 0){
                    Log.d("poipoi","poipoipoi")
                    if(i > 0 && list[i-1].type == "header"){
                        Log.d("uhaha","uhaha")
                     newGroup =  (list[i-1] as Header).group
                        break
                    }
                }
            }
            if (newPosition > oldPosition){
                newGroup = (list[newPosition] as Header).group
            }
            if(newGroup != "")
                mvpView.getPresenter().onGroupChanged(newGroup,
                    (list[oldPosition] as UserInSubGroup).id)
        }
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

    fun update(item: Typed){
        list.add(item)
        notifyItemInserted(list.size-1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return if(viewType == TYPE_USER)
            MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user_subgroup, parent, false))
        else
            MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false))

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

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var type = "person"
        fun bind(typed: Typed){
            if(typed.type == "person"){
                val user = typed as UserInSubGroup
                itemView.fieldName.text = user.name
                itemView.iconDrag.setImageDrawable(IconicsDrawable(mvpView.getCtx())
                        .icon(FontAwesome.Icon.faw_bars)
                        .sizeDp(16)
                        .color(ContextCompat.getColor(mvpView.getCtx(),R.color.white)))
                itemView.iconDrag.setOnTouchListener { view, motionEvent ->
                    if (motionEvent.actionMasked == MotionEvent.ACTION_DOWN) {
                        touchHelper.startDrag(this)
                    }
                    return@setOnTouchListener false
                }
            }else if(typed.type == "header"){
                type = "header"
                val header = typed as Header
                itemView.textTitle.text = header.group
            }
        }
    }

    interface Listener{
        fun onGroupChanged(changedGroup: String, changingId: String)
    }

    companion object {
        const val TYPE_USER = 0
        const val TYPE_HEADER = 1
    }
}