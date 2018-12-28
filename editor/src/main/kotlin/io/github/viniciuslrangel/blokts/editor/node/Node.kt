package io.github.viniciuslrangel.blokts.editor.node

import io.github.viniciuslrangel.blokts.editor.pin.Pin
import io.github.viniciuslrangel.blokts.util.Point
import javafx.scene.canvas.GraphicsContext

/**
 * Created by <viniciuslrangel> on 23 Dec 2018, 3:33 AM (UTC-3).
 */
interface Node {

    val pins: Array<Pin<*, *>>

    var prefWidth: Double
    var prefHeight: Double

    val draggable: Boolean

    val topPadding: Double

    fun render(context: GraphicsContext, size: Point)

}