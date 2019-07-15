package com.example.starratingview

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Property
import android.view.View

/**
 * Created by ngthailam on 15/07/2019
 */
class DotsView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : View(ctx, attrs, defStyleRes) {
    private val circlePaints = arrayOfNulls<Paint>(4)

    private var centerX: Int = 0
    private var centerY: Int = 0

    private var maxOuterDotsRadius: Float = 0f
    private var maxInnerDotsRadius: Float = 0f
    private var maxDotSize: Float = 0f

    private var currentProgress = 0f

    private var currentRadius1 = 0f
    private var currentDotSize1 = 0f

    private var currentDotSize2 = 0f
    private var currentRadius2 = 0f

    private val argbEvaluator = ArgbEvaluator()

    companion object {
        private const val DOTS_COUNT = 7
        private const val OUTER_DOTS_POSITION_ANGLE = 360 / DOTS_COUNT

        private const val COLOR_1 = -0x3ef9
        private const val COLOR_2 = -0x6800
        private const val COLOR_3 = -0xa8de
        private const val COLOR_4 = -0xbbcca

        val DOTS_PROGRESS: Property<DotsView, Float> = object :
            Property<DotsView, Float>(Float::class.java, "dotsProgress") {
            override fun get(`object`: DotsView): Float {
                return `object`.getCurrentProgress()
            }

            override fun set(`object`: DotsView, value: Float?) {
                `object`.setCurrentProgress(value!!)
            }
        }
    }

    init {
        for (i in 0 until circlePaints.size) {
            circlePaints[i] = Paint().apply {
                style = Paint.Style.FILL
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2
        centerY = h / 2
        maxDotSize = 15f
        maxOuterDotsRadius = centerX - maxDotSize * 2
        maxInnerDotsRadius = 0.8f * maxOuterDotsRadius // some magic number
    }

    override fun onDraw(canvas: Canvas?) {
        drawOuterDots(canvas)
        drawInnerDots(canvas)
    }

    private fun drawInnerDots(canvas: Canvas?) {
        for (i in 0 until DOTS_COUNT) {
            val cX =
                (centerX + currentRadius1 * Math.cos(i * OUTER_DOTS_POSITION_ANGLE * Math.PI / 180)).toInt()
            val cY =
                (centerY + currentRadius1 * Math.sin(i.toDouble() * OUTER_DOTS_POSITION_ANGLE.toDouble() * Math.PI / 180)).toInt()
            canvas?.drawCircle(
                cX.toFloat(),
                cY.toFloat(),
                currentDotSize1,
                circlePaints[i % circlePaints.size]!!
            )
        }
    }

    private fun drawOuterDots(canvas: Canvas?) {
        for (i in 0 until DOTS_COUNT) {
            val cX =
                (centerX + currentRadius2 * Math.cos((i * OUTER_DOTS_POSITION_ANGLE - 10) * Math.PI / 180)).toInt()
            val cY =
                (centerY + currentRadius2 * Math.sin((i * OUTER_DOTS_POSITION_ANGLE - 10) * Math.PI / 180)).toInt()
            canvas?.drawCircle(
                cX.toFloat(),
                cY.toFloat(),
                currentDotSize2,
                circlePaints[(i + 1) % circlePaints.size]!!
            )
        }
    }

    fun setCurrentProgress(currentProgress: Float) {
        this.currentProgress = currentProgress

        updateInnerDotsPosition()
        updateOuterDotsPosition()
        updateDotsPaints()
        updateDotsAlpha()

        postInvalidate()
    }

    fun getCurrentProgress(): Float {
        return currentProgress
    }

    private fun updateInnerDotsPosition() {
        if (currentProgress < 0.3f) {
            this.currentRadius2 =
                Utils.mapValueFromRangeToRange(currentProgress, 0f, 0.3f, 0f, maxInnerDotsRadius)
        } else {
            this.currentRadius2 = maxInnerDotsRadius
        }

        when {
            currentProgress < 0.2 -> this.currentDotSize2 = maxDotSize
            currentProgress < 0.5 -> this.currentDotSize2 = Utils.mapValueFromRangeToRange(
                currentProgress,
                0.2f,
                0.5f,
                maxDotSize,
                0.3f * maxDotSize
            )
            else -> this.currentDotSize2 =
                Utils.mapValueFromRangeToRange(currentProgress, 0.5f, 1f, maxDotSize * 0.3f, 0f)
        }
    }

    private fun updateOuterDotsPosition() {
        if (currentProgress < 0.3f) {
            this.currentRadius1 =
                Utils.mapValueFromRangeToRange(
                    currentProgress,
                    0.0f,
                    0.3f,
                    0f,
                    maxOuterDotsRadius * 0.8f
                )
        } else {
            this.currentRadius1 = Utils.mapValueFromRangeToRange(
                currentProgress,
                0.3f,
                1f,
                0.8f * maxOuterDotsRadius,
                maxOuterDotsRadius
            )
        }

        if (currentProgress < 0.7) {
            this.currentDotSize1 = maxDotSize
        } else {
            this.currentDotSize1 =
                Utils.mapValueFromRangeToRange(currentProgress, 0.7f, 1f, maxDotSize, 0f)
        }
    }

    private fun updateDotsPaints() {
        if (currentProgress < 0.5f) {
            val progress = Utils.mapValueFromRangeToRange(currentProgress, 0f, 0.5f, 0f, 1f)
            circlePaints[0]?.color = argbEvaluator.evaluate(progress, COLOR_1, COLOR_2) as Int
            circlePaints[1]?.color = argbEvaluator.evaluate(progress, COLOR_2, COLOR_3) as Int
            circlePaints[2]?.color = argbEvaluator.evaluate(progress, COLOR_3, COLOR_4) as Int
            circlePaints[3]?.color = argbEvaluator.evaluate(progress, COLOR_4, COLOR_1) as Int
        } else {
            val progress = Utils.mapValueFromRangeToRange(currentProgress, 0.5f, 1f, 0f, 1f)
            circlePaints[0]?.color = argbEvaluator.evaluate(progress, COLOR_2, COLOR_3) as Int
            circlePaints[1]?.color = argbEvaluator.evaluate(progress, COLOR_3, COLOR_4) as Int
            circlePaints[2]?.color = argbEvaluator.evaluate(progress, COLOR_4, COLOR_1) as Int
            circlePaints[3]?.color = argbEvaluator.evaluate(progress, COLOR_1, COLOR_2) as Int
        }
    }

    private fun updateDotsAlpha() {
        val progress = Utils.clamp(currentProgress, 0.6f, 1f)
        val alpha = Utils.mapValueFromRangeToRange(progress, 0.6f, 1f, 255f, 0f).toInt()
        circlePaints[0]?.alpha = alpha
        circlePaints[1]?.alpha = alpha
        circlePaints[2]?.alpha = alpha
        circlePaints[3]?.alpha = alpha
    }
}
