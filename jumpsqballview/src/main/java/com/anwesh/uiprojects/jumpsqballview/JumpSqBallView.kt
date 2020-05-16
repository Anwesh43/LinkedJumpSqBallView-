package com.anwesh.uiprojects.jumpsqballview

/**
 * Created by anweshmishra on 16/05/20.
 */

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.*

val nodes : Int = 5
val parts : Int = 2
val scGap : Float = 0.02f
val rFactor : Float = 3.2f
val sizeFactor : Float = 2.9f
val foreColor : Int = Color.parseColor("#673AB7")
val backColor : Int = Color.parseColor("#BDBDBD")
val rotDeg : Float = 180f
val sqHFactor : Float = 4f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()

fun Canvas.drawRotatingSqBall(scale : Float, size : Float, h : Float, paint : Paint) {
    val sf : Float = scale.sinify()
    val sf1 : Float = sf.divideScale(0, parts)
    val sf2 : Float = sf.divideScale(1, parts)
    val r : Float = size / rFactor
    val sqSize : Float = 2 * size - 2 * r
    val x : Float = -size
    save()
    rotate(rotDeg * sf1)
    drawRect(RectF(-size + 2 * r, -sqSize / (2 * sqHFactor), x + 2 * r + 2 * sqSize, sqSize / (2 * sqHFactor)), paint)
    drawCircle(x + r,  (0.5f * h - r) * sf2, r, paint)
    restore()
}

fun Canvas.drawRSBNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = w / (nodes + 1)
    val size : Float = gap / sizeFactor
    paint.color = foreColor
    save()
    translate(gap * (i + 1), h / 2)
    drawRotatingSqBall(scale, size, h, paint)
    restore()
}

class JumpSqBallView(ctx : Context) : View(ctx) {

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }
}