package com.stenleone.stanleysfilm.util.customView

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NonSwipeablePager : ViewPager {

    constructor(context: Context) : super(context)

    constructor(context: Context, attr: AttributeSet?) : super(context, attr)

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean = false

    override fun onTouchEvent(event: MotionEvent?): Boolean = false

}