package com.kingartur1000.lab56

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator

private val overshoot = OvershootInterpolator(3f)

private fun View.applyPressScale() {
    setOnTouchListener { v, event ->
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> v.animate()
                .scaleX(0.7f).scaleY(0.7f)
                .setDuration(80)
                .setInterpolator(null)
                .start()

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> v.animate()
                .scaleX(1f).scaleY(1f)
                .setDuration(180)
                .setInterpolator(overshoot)
                .start()
        }
        false
    }
}

fun View.applyPressScaleRecursively() {
    if (this is android.widget.Button) applyPressScale()
    if (this is ViewGroup) {
        for (i in 0 until childCount) getChildAt(i).applyPressScaleRecursively()
    }
}