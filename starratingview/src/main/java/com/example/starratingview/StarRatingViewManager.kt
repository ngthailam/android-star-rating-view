package com.example.starratingview

interface StarRatingViewManager {
    fun getTotalNumStar(): Int
    fun getNumStarSelected(): Int
    fun disableAnimation()
    fun enableAnimation()
    fun unselectAllStars()
    fun setSelectedStarNum(num: Int)
}