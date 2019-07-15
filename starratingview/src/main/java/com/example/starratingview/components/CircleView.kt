package com.example.starratingview.components

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.util.Log
import android.util.Property
import android.view.View
import com.example.starratingview.Utils

/**
 * Created by ngthailam on 14/07/2019
 */
class CircleView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : View(ctx, attrs, defStyleRes) {

    private val argbEvaluator = ArgbEvaluator()
    private val circlePaint = Paint()
    private val maskPaint = Paint()
    private lateinit var tempBitmap: Bitmap
    private lateinit var tempCanvas: Canvas
    private var maxCircleRadius: Int = 0
    // customizable values in the future
    private var startColor =
        DEFAULT_START_COLOR
    private var endColor =
        DEFAULT_END_COLOR
    private var outerCircleCurrentRadius: Float = 0f
    private var innerCircleCurrentRadius: Float = 0f

    companion object {
        const val TAG = "CircleView"
        const val DEFAULT_START_COLOR = Color.RED
        const val DEFAULT_END_COLOR = Color.BLUE
        val INNER_CIRCLE_CURRENT_RADIUS: Property<CircleView, Float> =
            object : Property<CircleView, Float>(Float::class.java, "innerCircleCurrentRadius") {
                override fun get(p0: CircleView): Float {
                    Log.d(TAG, "inner circle radius - get() ")
                    return p0.getInnerCircleCurrentRadius()
                }

                override fun set(`object`: CircleView?, value: Float) {
                    Log.d(TAG, "inner circle radius - set() ")
                    `object`?.setInnerCircleCurrentRadius(value)
                }
            }

        val OUTER_CIRCLE_RADIUS_PROGRESS: Property<CircleView, Float> =
            object : Property<CircleView, Float>(Float::class.java, "outerCircleRadiusProgress") {
                override fun get(p0: CircleView): Float {
                    return p0.getOuterCircleCurrentRadius()
                }

                override fun set(`object`: CircleView, value: Float) {
                    `object`.setOuterCircleCurrentRadius(value)
                }
            }
    }

    init {
        circlePaint.apply {
            Paint.Style.FILL
        }
        maskPaint.run {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
        startColor = DEFAULT_START_COLOR
        endColor =
            DEFAULT_END_COLOR // will change to accept customize color
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        maxCircleRadius = w / 2
        tempBitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
        tempCanvas = Canvas(tempBitmap)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val centerX = (width / 2).toFloat()
        val centerY = (height / 2).toFloat()
        val outerCircleRadius = outerCircleCurrentRadius * maxCircleRadius
        val innerCircleRadius = innerCircleCurrentRadius * maxCircleRadius
        tempCanvas.run {
            drawColor(0xffffff, PorterDuff.Mode.CLEAR)
            drawCircle(centerX, centerY, outerCircleRadius, circlePaint)
            drawCircle(centerX, centerY, innerCircleRadius, maskPaint)
        }
        canvas?.drawBitmap(tempBitmap, 0f, 0f, null)
    }

    fun setInnerCircleCurrentRadius(radius: Float) {
        innerCircleCurrentRadius = radius
        postInvalidate()
    }

    fun getInnerCircleCurrentRadius(): Float = innerCircleCurrentRadius

    fun setOuterCircleCurrentRadius(radius: Float) {
        outerCircleCurrentRadius = radius
        updateCircleColor()
        postInvalidate()
    }

    fun getOuterCircleCurrentRadius(): Float = outerCircleCurrentRadius

    private fun updateCircleColor() {
        val colorProgress = Utils.mapValueFromRangeToRange(
            Utils.clamp(outerCircleCurrentRadius, 0.5f, 1f),
            0.5f,
            1f,
            0f,
            1f
        )
        Log.d(TAG, "updateCircleColor(): colorProgress: $colorProgress")
        circlePaint.color =
            argbEvaluator.evaluate(
                colorProgress,
                startColor,
                endColor
            ) as Int
    }
}
