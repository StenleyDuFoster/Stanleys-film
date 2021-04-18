package com.stenleone.stanleysfilm.util.anim

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.TransitionDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.stenleone.stanleysfilm.R

class ButtonTextColorAnimator(
    context: Context,
    private val buttons: ArrayList<View> = arrayListOf(),
    private val TRANSITION_DURATION: Int = 400,
    startTextColor: Int? = null,
    endTextColor: Int? = null,
    startViewColor: Int? = null,
    endViewColor: Int? = null
) {

    private val localStartTextColor: Int
    private val localEndTextColor: Int
    private val localStartViewColor: Int
    private val localEndViewColor: Int

    init {
        localStartTextColor = startTextColor ?: ContextCompat.getColor(context, R.color.transparent)
        localEndTextColor = endTextColor ?: ContextCompat.getColor(context, R.color.active)
        localStartViewColor = startViewColor ?: ContextCompat.getColor(context, R.color.transparent)
        localEndViewColor = endViewColor ?: ContextCompat.getColor(context, R.color.active)
    }

    fun click(view: View, withText: Boolean = false) {
        if (buttons.contains(view)) {
            buttons.remove(view)
            toInActive(view, withText)
        } else {
            buttons.add(view)
            toActive(view, withText)
        }
    }

    fun clickAndInActiveOther(view: View, withText: Boolean = false) {
        buttons.forEach { button ->
            toInActive(button, withText)
            buttons.remove(button)
        }

        buttons.add(view)
        toActive(view, withText)
    }

    fun toActive(view: View, withText: Boolean = false) {
        (view.background as? TransitionDrawable)?.startTransition(TRANSITION_DURATION)
        if (withText) {
            animateTextToEndColor(view, localStartTextColor, localEndTextColor)
        }
    }

    fun toInActive(view: View, withText: Boolean = false) {
        (view.background as? TransitionDrawable)?.reverseTransition(TRANSITION_DURATION)
        if (withText) {
            animateTextToEndColor(view, localEndTextColor, localStartTextColor)
        }
    }

    private fun animateTextToEndColor(view: View, startTextColor: Int, endTextColor: Int) {
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), startTextColor, endTextColor)
        colorAnimation.duration = TRANSITION_DURATION.toLong()
        colorAnimation.addUpdateListener { animator -> ((view as? ViewGroup)?.getChildAt(0) as? TextView)?.setTextColor(animator.animatedValue as Int) }
        colorAnimation.start()
    }
}