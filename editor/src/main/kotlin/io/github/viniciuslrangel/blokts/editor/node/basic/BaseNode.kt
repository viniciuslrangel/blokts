package io.github.viniciuslrangel.blokts.editor.node.basic

import io.github.viniciuslrangel.blokts.editor.node.Node
import io.github.viniciuslrangel.blokts.editor.pin.Pin
import io.github.viniciuslrangel.blokts.util.Point
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.Font

/**
 * Created by <viniciuslrangel> on 23 Dec 2018, 3:02 PM (UTC-3).
 */
abstract class BaseNode : Node {

    override val pins: Array<Pin<*, *>> = emptyArray()

    override var prefWidth = 130.0
    override var prefHeight = 70.0

    override val draggable = true

    open val font = Font(16.0)

    override val topPadding: Double
        get() = font.size + 1.0

    abstract val title: String
    open val titleColor: Paint = Color.WHITE
    open val titleBackground: Paint = Color.DARKGRAY

    open val background: Paint = Color.LIGHTGRAY

    override fun render(context: GraphicsContext, size: Point) {

        context.fill = background
        context.fillRoundRect(0.0, 0.0, size.x, size.y, 4.0, 4.0)

        context.fill = titleBackground
        context.fillRoundRect(4.0, 4.0, size.x - 8.0, 8.0 + font.size, 4.0, 4.0)

        context.fill = titleColor
        context.font = font
        context.fillText(title, 8.0, font.size + 6.0)

    }
}