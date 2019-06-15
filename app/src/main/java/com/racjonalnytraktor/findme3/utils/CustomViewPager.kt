package com.racjonalnytraktor.findme3.utils

import android.content.Context
import android.view.MotionEvent
import android.text.method.Touch.onTouchEvent
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet


class CustomViewPager : ViewPager {

    private var isPagingEnabled = true

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return false
    }

    fun setPagingEnabled(b: Boolean) {
        this.isPagingEnabled = b
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        super.setCurrentItem(item, false)
    }

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item, false)
    }
}