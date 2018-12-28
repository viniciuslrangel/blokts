package io.github.viniciuslrangel.blokts.util


/**
 * Created by <viniciuslrangel> on 21 Dec 2018, 10:53 PM (UTC-3).
 */

data class Point(val x: Double, val y: Double) {
    companion object {
        val ZERO = Point(0.0, 0.0)
    }

    operator fun plus(other: Point) = Point(x + other.x, y + other.y)

    operator fun minus(other: Point) = Point(x - other.x, y - other.y)

    operator fun compareTo(other: Point): Int {
        return if (x != other.x) x.compareTo(other.x) else y.compareTo(other.y)
    }

}

infix fun Double.point(other: Double) = Point(this, other)

data class Rect(val p1: Point, val p2: Point) {

    companion object {
        fun fromSize(p1: Point, size: Point) = Rect(p1, p1 + size)
    }

    val size: Point
        get() = Point(p2.x - p1.x, p2.y - p1.y)

    fun isInside(point: Point) =
        point.x >= p1.x && point.x <= p2.x && point.y >= p1.y && point.y <= p2.y

}