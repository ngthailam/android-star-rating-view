package com.example.starratingview

interface StarRatingViewManager {
    fun getTotalNumStar(): Int
    fun getNumStarSelected(): Int
    fun disableAnimation()
    fun enableAnimation()
    fun unselectAllStars()
    fun enableDots()
    fun enableCircle()
    fun disableDots()
    fun disableCircle()
    fun setSelectedStarNum(num: Int)
    fun setNumStars(num: Int)
    fun setStarSize(size: Int)
    fun setCircleStartColor(color: Int)
    fun setCircleEndColor(color: Int)
}