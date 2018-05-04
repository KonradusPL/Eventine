package com.racjonalnytraktor.findme3.utils

import android.animation.ObjectAnimator
import android.util.Log
import android.view.View
import com.racjonalnytraktor.findme3.R
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

object AnimationHelper {

    val animationTime: Long = 300
    var screenWidth = 0

    private var mDistanceBetweenBtn = 0f

   /* fun moveView(type: String, majorView: View, minorView: View){
        var distance = majorView.x - minorView.x
        if(majorView.id == R.id.buttonStart && distance == 0.0f)
            distance = mDistanceBetweenBtn

        mDistanceBetweenBtn = distance

        val animation: ObjectAnimator
        if(type.equals("left"))
            animation = ObjectAnimator.ofFloat(majorView, "translationX", -distance)
        else //if(type.equals("return"))
            animation = ObjectAnimator.ofFloat(majorView, "translationX",
                    0f)

        animation.duration = animationTime
        animation.start()
    }*/

    fun showView(view: View){
        view.visibility = View.VISIBLE
        val animation = ObjectAnimator.ofFloat(view, "alpha", 1f)
        animation.duration = animationTime
        animation.start()
    }

    fun hideView(view: View){
        val animation = ObjectAnimator.ofFloat(view, "alpha", 0f)
        animation.duration = animationTime
        animation.start()
        doAsync {
            Thread.sleep(animationTime)
            uiThread { view.visibility = View.INVISIBLE }
        }
    }

    fun hideViewFast(view: View){
        val animation = ObjectAnimator.ofFloat(view, "alpha", 0f)
        animation.duration = 0
        animation.start()
    }
}