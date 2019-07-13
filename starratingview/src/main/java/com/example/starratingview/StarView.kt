package com.example.starratingview

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.animation.addListener
import androidx.core.animation.doOnCancel
import kotlinx.android.synthetic.main.star_view.view.*
import java.lang.IllegalArgumentException

class StarView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : FrameLayout(ctx, attrs, defStyleRes) {

    // TODO: impelement isAnimated
    companion object {
        val DECELERATE_INTERPOLATOR = DecelerateInterpolator()
        val ACCELERATE_INTERPOLATOR = AccelerateInterpolator()
        val OVERSHOOT_INTERPOLATOR = OvershootInterpolator()
        //
        const val DEFAULT_IS_ANIMATED = true
    }

    private var state: String = StarState.UNCHECKED
    private var animatorSet: AnimatorSet? = null
    private var isAnimated: Boolean = true

    init {
        View.inflate(ctx, R.layout.star_view, this)
        //
        @SuppressLint("CustomViewStyleable")
        val typedArray = ctx.obtainStyledAttributes(attrs, R.styleable.StarRatingView)
        isAnimated = typedArray.getBoolean(R.styleable.StarRatingView_enableStarAnim, DEFAULT_IS_ANIMATED)
        typedArray.recycle()
    }

    fun animateToChecked() {
        ivStarViewStar.setImageResource(R.drawable.ic_star_rate_on)
        setState(StarState.CHECKED)

        if (!isAnimated) return

        ivStarViewStar.animate().cancel()
        ivStarViewStar.scaleX = 0f
        ivStarViewStar.scaleY = 0f
        //
        animatorSet = AnimatorSet()
        //
        val starScaleXAnimator =
            ObjectAnimator.ofFloat(ivStarViewStar, ImageView.SCALE_Y, 0.2f, 1f).apply {
                duration = 350
                startDelay = 250
                interpolator = OVERSHOOT_INTERPOLATOR
            }
        val starScaleYAnimator =
            ObjectAnimator.ofFloat(ivStarViewStar, ImageView.SCALE_X, 0.2f, 1f).apply {
                duration = 350
                startDelay = 250
                interpolator = OVERSHOOT_INTERPOLATOR
            }
        //
        animatorSet?.playTogether(
            listOf(
                starScaleXAnimator,
                starScaleYAnimator
            )
        )
        animatorSet?.addListener {
            it.doOnCancel {
                ivStarViewStar.run {
                    scaleX = 1f
                    scaleY = 1f
                }
            }
            setState(StarState.UNCHECKED)
        }
        animatorSet?.start()
    }

    fun animateToUnchecked() {
        ivStarViewStar.setImageResource(R.drawable.ic_star_rate_off)
        setState(StarState.UNCHECKED)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isAnimated) {
                    ivStarViewStar.animate().run {
                        scaleX(0.7f)
                        scaleY(0.7f)
                        duration = 150
                        interpolator = DECELERATE_INTERPOLATOR
                    }
                }
                isPressed = true
            }
            MotionEvent.ACTION_UP -> {
                if (isAnimated) {
                    ivStarViewStar.animate().run {
                        scaleX(1f)
                        scaleY(1f)
                        interpolator = DECELERATE_INTERPOLATOR
                    }
                }
                if (isPressed) {
                    performClick()
                    isPressed = false
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val x = event.x
                val y = event.y
                val isInside = x > 0 && x < width && y > 0 && y < height
                if (isPressed != isInside) {
                    isPressed = isInside
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                if (isAnimated) {
                    ivStarViewStar.animate().run {
                        scaleX(1f)
                        scaleY(1f)
                        interpolator = DECELERATE_INTERPOLATOR
                    }
                }
                isPressed = false
            }
        }
        return true
    }

    @Throws(IllegalArgumentException::class)
    fun setState(@StarState state: String) {
        this.state = state
    }

    fun getState() = this.state
}
