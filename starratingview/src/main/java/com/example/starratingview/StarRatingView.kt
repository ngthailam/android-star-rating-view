package com.example.starratingview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.starratingview.components.StarView
import kotlinx.android.synthetic.main.star_rating_view.view.*
import java.lang.IndexOutOfBoundsException

class StarRatingView @JvmOverloads constructor(
    private val ctx: Context,
    private val attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : LinearLayout(ctx, attrs, defStyleRes), StarRatingViewManager {

    companion object {
        const val DEFAULT_NUM_STAR = 5
        const val DEFAULT_STAR_SIZE = 50
    }

    private var totalStarNum: Int = 0
    private var selectedStarNum: Int = 0
    private var enableAnimation: Boolean = true
    private var starSize = 0

    init {
        View.inflate(ctx, R.layout.star_rating_view, this)
        val typedArray = ctx.obtainStyledAttributes(attrs, R.styleable.StarRatingView)
        totalStarNum = typedArray.getInt(R.styleable.StarRatingView_svNumStar, DEFAULT_NUM_STAR)
        starSize = typedArray.getInt(R.styleable.StarRatingView_svSize, DEFAULT_STAR_SIZE)
        typedArray.recycle()
        setupUI()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measuredWidth = paddingLeft + paddingRight + (starSize * totalStarNum)
        val measuredHeight = paddingTop + paddingBottom + starSize
        super.onMeasure(measuredWidth, measuredHeight)
    }

    private fun setupUI() {
        // TODO: may need to merge 2 for loops ( if possible )
        for (item in 0 until totalStarNum) {
            // TODO: pass only needed attrs to starview
            val starView = StarView(ctx, attrs, 0)
            starView.layoutParams = ViewGroup.LayoutParams(starSize, starSize)
            llStarRatingView.addView(starView)
        }
        // set click listener for all starts
        for (viewIndex in 0 until totalStarNum) {
            llStarRatingView.getChildAt(viewIndex).setOnClickListener {
                val currStar = viewIndex + 1
                setSelectedStarNum(currStar)
            }
        }
    }

    private fun animateStarOnSelectedPosition(position: Int) {
        if (position < 0 || position > totalStarNum) throw IndexOutOfBoundsException()
        for (index in 0 until position) {
            val starView: View? = llStarRatingView?.getChildAt(index)
            if (starView is StarView) starView.animateToChecked()
        }
        for (index in position until totalStarNum) {
            val starView: View? = llStarRatingView?.getChildAt(index)
            if (starView is StarView) starView.animateToUnchecked()
        }
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
        setSelectedStarNum(0)
    }

    override fun setSelectedStarNum(num: Int) {
        animateStarOnSelectedPosition(num)
        this.selectedStarNum = num
    }

    override fun setNumStars(num: Int) {
        this.totalStarNum = num
    }

    override fun setStarSize(size: Int) {
        this.starSize = size
    }

    override fun enableDots() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enableCircle() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun disableDots() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun disableCircle() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setCircleStartColor(color: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setCircleEndColor(color: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
