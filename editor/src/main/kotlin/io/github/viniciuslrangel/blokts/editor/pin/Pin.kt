package io.github.viniciuslrangel.blokts.editor.pin

import io.github.viniciuslrangel.blokts.util.point
import javafx.scene.Node
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSuperclassOf

/**
 * Created by <viniciuslrangel> on 23 Dec 2018, 3:51 AM (UTC-3).
 */
sealed class Pin<T : Any, K : Any>(val type: PinType<T>, val side: PinSide) {

    var node: Node? = null

    open val size = 20.0 point 20.0

    abstract val isConnected: Boolean

    abstract fun attachToPin(node: Pin<*, *>): Boolean

    abstract fun detach()

    fun render(context: GraphicsContext, selected: Boolean) {
        val width = context.canvas.width
        val height = context.canvas.height

        if(context.canvas.isHover) {
            context.fill = Color.WHITE
            context.fillOval(2.0, 2.0, width - 4.0, height - 4.0)
        }
        context.fill = type.color
        context.fillOval(4.0, 4.0, width - 8.0, height - 8.0)

        context.fillPolygon(
            doubleArrayOf(width       , width  * 0.7, width  * 0.9, width   * 0.7 ),
            doubleArrayOf(height * 0.5, height * 0.8, height * 0.5, height  * 0.2 ),
            4
        )

        if(!(selected || isConnected)) {
            context.fill = Color.BLACK
            context.fillOval(6.0, 6.0, width - 12.0, height - 12.0)
        }
    }

}

enum class PinSide {
    RIGHT, LEFT
}

open class OutputPin<T : Any>(type: PinType<T>) : Pin<T, InputPin<T>>(type, PinSide.RIGHT) {

    internal val attachedTo = mutableListOf<InputPin<out Any>>()

    override val isConnected: Boolean
        get() = attachedTo.isNotEmpty()

    override fun attachToPin(node: Pin<*, *>): Boolean {
        if(node is InputPin<*> && node.type.clazz.isSuperclassOf(type.clazz)) {
            if(node.attachToPin(this)) {
                attachedTo += node
            }
        }
        return false
    }

    override fun detach() {
        attachedTo.forEach(InputPin<out Any>::detach)
        attachedTo.clear()
    }

}

open class InputPin<T : Any>(type: PinType<T>) : Pin<T, OutputPin<T>>(type, PinSide.LEFT) {

    var attachedTo: OutputPin<T>? = null
        private set

    override val isConnected: Boolean
        get() = attachedTo != null

    override fun attachToPin(node: Pin<*, *>): Boolean {
        if(node is OutputPin && node.type.clazz.isSubclassOf(type.clazz)) {
            @Suppress("UNCHECKED_CAST")
            attachedTo = node as OutputPin<T>
            node.attachedTo += this
            return true
    }
        return false
    }

    override fun detach() {
        attachedTo?.attachedTo?.remove(this)
        attachedTo = null
    }

}