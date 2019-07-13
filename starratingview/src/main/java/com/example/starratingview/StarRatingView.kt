package com.example.starratingview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.star_rating_view.view.*

class StarRatingView @JvmOverloads constructor(
    private val ctx: Context,
    private val attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : LinearLayout(ctx, attrs, defStyleRes), View.OnClickListener, StarRatingViewManager {

    // TODO: core funcs: animate the prev stars
    // TODO: implement manager methods
    companion object {
        const val DEFAULT_NUM_STAR = 5
    }

    private var totalStarNum: Int = 0
    private var selectedStarNum: Int = 0
    private var enableAnimation: Boolean = true

    init {
        View.inflate(ctx, R.layout.star_rating_view, this)
        setOnClickListener(this)
        val typedArray = ctx.obtainStyledAttributes(attrs, R.styleable.StarRatingView)
        totalStarNum = typedArray.getInt(R.styleable.StarRatingView_numStar, DEFAULT_NUM_STAR)
        typedArray.recycle()
        setupUI()
    }

    private fun setupUI() {
        for (item in 0 until totalStarNum) {
            val starView = StarView(ctx, attrs, 0)
            llStarRatingView.addView(starView)
        }
        for (viewIndex in 0 until totalStarNum) {
            llStarRatingView.getChildAt(viewIndex).setOnClickListener {
                val currStar = viewIndex + 1
                setSelectedStarNum(currStar)
                for (index in 0 until currStar) {
                    (llStarRatingView.getChildAt(index) as StarView).animateToChecked()
                }
                for (index in currStar until totalStarNum) {
                    (llStarRatingView.getChildAt(index) as StarView).animateToUnchecked()
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        // do nothing
    }

    override fun getTotalNumStar(): Int = totalStarNum

    override fun getNumStarSelected(): Int = selectedStarNum

    override fun disableAnimation() {
        enableAnimation = false
    }

    override fun enableAnimation() {
        enableAnimation = true
    }

    override fun unselectAllStars() {
        for (index in 0..totalStarNum) {
            if (llStarRatingView.getChildAt(index) is StarView) {
                (llStarRatingView.getChildAt(index) as StarView).animateToUnchecked()
            }
        }
        setSelectedStarNum(0)
    }

    override fun setSelectedStarNum(num: Int) {
        this.selectedStarNum = num
    }
}
