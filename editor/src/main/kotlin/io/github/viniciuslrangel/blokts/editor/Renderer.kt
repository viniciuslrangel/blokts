package io.github.viniciuslrangel.blokts.editor

import io.github.viniciuslrangel.blokts.editor.node.basic.BaseNode
import io.github.viniciuslrangel.blokts.editor.node.basic.RootNodeContainer
import io.github.viniciuslrangel.blokts.editor.pin.InputPin
import io.github.viniciuslrangel.blokts.editor.pin.OutputPin
import io.github.viniciuslrangel.blokts.editor.pin.Pin
import io.github.viniciuslrangel.blokts.editor.pin.StandardPins
import io.github.viniciuslrangel.blokts.editor.state.NodeContainerState
import io.github.viniciuslrangel.blokts.editor.state.NodeState
import io.github.viniciuslrangel.blokts.util.BoxConstraint
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by <viniciuslrangel> on 23 Dec 2018, 3:42 PM (UTC-3).
 */
class Renderer {

    class TestNode() : BaseNode() {

        companion object {
            val num = AtomicInteger()
        }

        override val title: String
            get() = "Add ${num.getAndIncrement()}"

        override val pins: Array<Pin<*, *>> = arrayOf(
            InputPin(StandardPins.FLOAT),
            InputPin(StandardPins.INTEGER),
            InputPin(StandardPins.INTEGER),
            InputPin(StandardPins.BOOLEAN),
            OutputPin(StandardPins.INTEGER),
            OutputPin(StandardPins.BOOLEAN),
            OutputPin(StandardPins.FLOAT)
        )
    }

    private val rootState = NodeContainerState(
        RootNodeContainer(),
        constraint = BoxConstraint.expand(),
        children = mutableListOf(
            NodeState(TestNode()),
            NodeState(TestNode())
        )
    )

    fun build() = rootState.build()

    fun render() = rootState.render()

}