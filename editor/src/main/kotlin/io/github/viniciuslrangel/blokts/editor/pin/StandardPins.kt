package io.github.viniciuslrangel.blokts.editor.pin

import javafx.scene.paint.Color


/**
 * Created by <viniciuslrangel> on 23 Dec 2018, 9:17 PM (UTC-3).
 */
object StandardPins {

    private inline fun <reified T : Any> p(color: Color = Color.GRAY) = PinType(T::class, color)

    val ANY = p<Any>(Color.BLUE)

    val BOOLEAN = p<Boolean>(Color.RED.darker())

    val NUMERIC = p<Number>(Color.YELLOW)
    val INTEGER = p<Int>(Color.CYAN)
    val FLOAT = p<Float>(Color.GREEN)

    val STRING = p<String>(Color.MAGENTA)

}
