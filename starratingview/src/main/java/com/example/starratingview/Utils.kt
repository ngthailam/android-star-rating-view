package com.example.starratingview

import androidx.annotation.StringDef
import com.example.starratingview.StarState.Companion.ANIMATING
import com.example.starratingview.StarState.Companion.CHECKED
import com.example.starratingview.StarState.Companion.UNCHECKED

@StringDef(value = [CHECKED, UNCHECKED, ANIMATING])
@Retention(AnnotationRetention.SOURCE)
annotation class StarState {
    companion object {
        const val CHECKED = "CHECKED"
        const val UNCHECKED = "UNCHECKED"
        const val ANIMATING = "ANIMATING"
    }
}
