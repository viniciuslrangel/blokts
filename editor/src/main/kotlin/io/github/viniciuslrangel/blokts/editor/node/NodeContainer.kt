package io.github.viniciuslrangel.blokts.editor.node

import io.github.viniciuslrangel.blokts.util.Point
import javafx.scene.canvas.GraphicsContext

/**
 * Created by <viniciuslrangel> on 23 Dec 2018, 3:53 AM (UTC-3).
 */
interface NodeContainer : Node {

    fun render(context: GraphicsContext, size: Point, offsetX: Double, offsetY: Double)

}