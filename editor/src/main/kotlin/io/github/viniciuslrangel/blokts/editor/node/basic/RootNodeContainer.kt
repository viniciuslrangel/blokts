package io.github.viniciuslrangel.blokts.editor.node.basic

import io.github.viniciuslrangel.blokts.util.Point
import javafx.scene.canvas.GraphicsContext

/**
 * Created by <viniciuslrangel> on 23 Dec 2018, 3:40 PM (UTC-3).
 */
class RootNodeContainer : BaseNodeContainer() {
    override val title = "Container"

    override fun render(context: GraphicsContext, size: Point) {

        context.fill = background
        context.fillRoundRect(0.0, 0.0, size.x, size.y, 4.0, 4.0)

    }

}