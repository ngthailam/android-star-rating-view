package com.example.starratingview

import androidx.annotation.StringDef
import com.example.starratingview.StarState.Companion.ANIMATING
import com.example.starratingview.StarState.Companion.CHECKED
import com.example.starratingview.StarState.Companion.UNCHECKED

class Utils {
    companion object {
        fun clamp(value: Float, low: Float, high: Float) =
            Math.min(Math.max(value, low), high)

        fun mapValueFromRangeToRange(
            value: Float,
            fromLow: Float,
            fromHigh: Float,
            toLow: Float,
            toHigh: Float
        ) = (toLow + ((value - fromLow) / (fromHigh - fromLow) * (toHigh - toLow)))
    }
}

@StringDef(value = [CHECKED, UNCHECKED, ANIMATING])
@Retention(AnnotationRetention.SOURCE)
annotation class StarState {
    companion object {
        const val CHECKED = "CHECKED"
        const val UNCHECKED = "UNCHECKED"
        const val ANIMATING = "ANIMATING"
    }
}
