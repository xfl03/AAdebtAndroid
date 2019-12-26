package com.github.xfl03.aadebt.android.listener

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max

class FabTouchListener(private val context: Context) : View.OnTouchListener {
    private val sp = context.getSharedPreferences("fab", Context.MODE_PRIVATE)

    var lastX = 0f
    var lastY = 0f
    var parentWidth = 0
    var parentHeight = 0
    var downX = 0f
    var downY = 0f
    var maxDX = 0f
    var maxYX = 0f
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val parent = v.parent
        if (parent != null && parent is ViewGroup) {
            parentHeight = parent.height
            parentWidth = parent.width
        }
        val rawX = event.rawX
        val rawY = event.rawY
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = rawX
                lastY = rawY

                downX = rawX
                downY = rawY

                maxDX = 0f
                maxYX = 0f
                true
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = rawX - lastX
                val dy = rawY - lastY

                var x = v.x + dx
                var y = v.y + dy

                x = max(0f, min(x, parentWidth.toFloat() - v.width))
                y = max(0f, min(y, parentHeight.toFloat() - v.height))

                v.x = x
                v.y = y

                lastX = rawX
                lastY = rawY

                maxDX = max(abs(rawX - downX), maxDX)
                maxYX = max(abs(rawY - downY), maxYX)
                true
            }
            MotionEvent.ACTION_UP -> {
                if (maxDX < 10f && maxYX < 10f) {
                    v.performClick()
                }
                val x = if (rawX < parentWidth / 2) 60 else parentWidth - v.width - 60
                v.animate().x(x.toFloat()).setDuration(300).start()
                sp.edit().putFloat("x", x.toFloat()).putFloat("y", v.y).apply()
                true
            }
            else -> false
        }
    }
}