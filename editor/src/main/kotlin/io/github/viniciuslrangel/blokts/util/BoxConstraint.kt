package io.github.viniciuslrangel.blokts.util

/**
 * Created by <viniciuslrangel> on 24 Dec 2018, 3:47 PM (UTC-3).
 */
class BoxConstraint(
    val minWidth: Double = 0.0,
    val maxWidth: Double = Double.MAX_VALUE,
    val minHeight: Double = 0.0,
    val maxHeight: Double = Double.MAX_VALUE
) {

    companion object {
        fun expand(width: Double = Double.MAX_VALUE, height: Double = Double.MAX_VALUE) =
            BoxConstraint(width, width, height, height)
    }

    operator fun invoke(width: Double, height: Double): Point {
        return Math.max(minWidth, Math.min(maxWidth, width)) point Math.max(minHeight, Math.min(maxHeight, height))
    }
}