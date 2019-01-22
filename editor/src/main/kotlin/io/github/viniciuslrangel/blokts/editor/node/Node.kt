package io.github.viniciuslrangel.blokts.editor.node

import io.github.viniciuslrangel.blokts.editor.pin.Pin
import io.github.viniciuslrangel.blokts.util.Point
import javafx.scene.canvas.GraphicsContext
import javafx.scene.layout.AnchorPane

/**
 * Created by <viniciuslrangel> on 23 Dec 2018, 3:33 AM (UTC-3).
 */
interface Node {

    val pins: Array<Pin<*, *>>

    val prefWidth: Double
    val prefHeight: Double

    val draggable: Boolean

    val topPadding: Double

    fun render(context: GraphicsContext, size: Point)

    fun build(pane: AnchorPane) = Unit

    var rebuild: () -> Unit

}