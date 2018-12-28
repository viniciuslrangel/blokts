package io.github.viniciuslrangel.blokts.editor.node.basic

import io.github.viniciuslrangel.blokts.editor.node.NodeContainer
import io.github.viniciuslrangel.blokts.util.Point
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.*

/**
 * Created by <viniciuslrangel> on 23 Dec 2018, 3:24 PM (UTC-3).
 */
abstract class BaseNodeContainer : BaseNode(), NodeContainer {

    override val background = LinearGradient(
        0.0, 0.0, 0.0, 1.0, true, CycleMethod.NO_CYCLE,
        Stop(0.0, Color.GRAY),
        Stop(1.0, Color.GRAY.darker())
    )

    override var prefWidth = 600.0
    override var prefHeight = 400.0

    open val gridLine: Paint = Color.DARKGRAY

    override fun render(context: GraphicsContext, size: Point, offsetX: Double, offsetY: Double) {
        context.stroke = gridLine
        context.lineWidth = 1.0
        val w = size.x
        val h = size.y
        val step = 40L

        for (i in (offsetX % step).toLong() until w.toLong() step step)
            context.strokeLine(i.toDouble(), 0.0, i.toDouble(), h)

        for (i in (offsetY % step).toLong() until h.toLong() step step)
            context.strokeLine(0.0, i.toDouble(), w, i.toDouble())

    }

}