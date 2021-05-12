package net.simplifiedcoding.game2048

import android.app.Activity
import android.graphics.Point
import android.os.Build

fun Activity.getDisplayMetrics(): Point {
    val displayMetrics = Point()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        display?.getRealSize(displayMetrics)
    } else {
        val display = windowManager.defaultDisplay
        display.getRealSize(displayMetrics)
    }
    return displayMetrics
}