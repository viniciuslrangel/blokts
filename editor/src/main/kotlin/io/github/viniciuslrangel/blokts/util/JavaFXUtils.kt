package io.github.viniciuslrangel.blokts.util

//import io.github.viniciuslrangel.blokts.editor.node.Node
//import io.github.viniciuslrangel.blokts.editor.node.NodeContainer
import io.github.viniciuslrangel.blokts.editor.state.NodeContainerState
import io.github.viniciuslrangel.blokts.editor.state.NodeState
import javafx.scene.Node
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane

/**
 * Created by <viniciuslrangel> on 21 Dec 2018, 6:28 PM (UTC-3).
 */

object JavaFXUtils {

    object AnchorPanel {

        fun setFullAnchor(node: Node, value: Double = 0.0) {
            setAnchor(node, value, value, value, value)
        }

        fun setAnchor(node: Node, top: Double? = null, right: Double? = null, bottom: Double? = null, left: Double? = null) {
            if(top != null)
                AnchorPane.setTopAnchor(node, top)
            if(right != null)
                AnchorPane.setRightAnchor(node, right)
            if(bottom != null)
                AnchorPane.setBottomAnchor(node, bottom)
            if(left != null)
                AnchorPane.setLeftAnchor(node, left)
        }

    }

    object Drag {

        private var deltaX: Double = 0.0
        private var deltaY: Double = 0.0

        fun makeDraggable(component: Node, state: NodeState) { // FIXME When press right click while dragging make the state reset to initial position
            component.setOnMousePressed {
                if (!(it.isPrimaryButtonDown && state.node.draggable))
                    return@setOnMousePressed
                deltaX = component.layoutX - it.sceneX
                deltaY = component.layoutY - it.sceneY
            }

            component.setOnMouseDragged {
                if (!(it.isPrimaryButtonDown && state.node.draggable) || it.isSecondaryButtonDown)
                    return@setOnMouseDragged
                state.offsetX = deltaX + it.sceneX
                state.offsetY = deltaY + it.sceneY
                component.layoutX = state.offsetX
                component.layoutY = state.offsetY
            }
        }

        fun makeContainerDraggable(
            pane: Pane,
            state: NodeContainerState,
            onRightClick: (MouseEvent) -> Unit = {}
        ) {
            var dragging = false

            pane.setOnMousePressed {
                if (!(it.isSecondaryButtonDown))
                    return@setOnMousePressed
                dragging = false
                deltaX = state.offsetX - it.sceneX
                deltaY = state.offsetY - it.sceneY
            }

            pane.setOnMouseReleased {
                if(it.button == MouseButton.SECONDARY && !dragging) {
                    onRightClick(it)
                }
            }

            pane.setOnMouseDragged {
                if (!(it.isSecondaryButtonDown))
                    return@setOnMouseDragged
                dragging = true
                val lastOffset = state.offsetX to state.offsetY
                state.offsetX = deltaX + it.sceneX
                state.offsetY = deltaY + it.sceneY
                pane.childrenUnmodifiable.forEach { childPane ->
                    childPane.layoutX += state.offsetX - lastOffset.first
                    childPane.layoutY += state.offsetY - lastOffset.second
                }
                state.bgRender()
                state.fgRender()
            }
        }
    }

}