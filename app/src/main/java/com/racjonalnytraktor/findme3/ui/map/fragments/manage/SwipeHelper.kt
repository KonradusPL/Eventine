package com.racjonalnytraktor.findme3.ui.map.fragments.manage

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.racjonalnytraktor.findme3.ui.adapters.ManageAdapter

class SwipeHelper: ItemTouchHelper.Callback() {

    var contract: ActionCompletionContract? = null

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        if ((viewHolder as ManageAdapter.MyViewHolder).type == "header") {
            return 0
        }
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        contract!!.onViewMoved(viewHolder!!.getAdapterPosition(), target!!.getAdapterPosition());
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        contract!!.onViewSwiped(viewHolder!!.getAdapterPosition());
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun onChildDraw(c: Canvas,
                             recyclerView: RecyclerView,
                             viewHolder: RecyclerView.ViewHolder,
                             dX: Float,
                             dY: Float,
                             actionState: Int,
                             isCurrentlyActive: Boolean) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val alpha = 1 - Math.abs(dX) / recyclerView.width
            viewHolder.itemView.alpha = alpha
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    interface ActionCompletionContract {
        fun onViewMoved(oldPosition: Int, newPosition: Int)

        fun onViewSwiped(position: Int)
    }
}