package io.github.viniciuslrangel.blokts.editor.state

import io.github.viniciuslrangel.blokts.editor.node.Node
import io.github.viniciuslrangel.blokts.editor.node.NodeContainer
import io.github.viniciuslrangel.blokts.editor.pin.InputPin
import io.github.viniciuslrangel.blokts.editor.pin.Pin
import io.github.viniciuslrangel.blokts.editor.pin.PinSide
import io.github.viniciuslrangel.blokts.generated.GeneratedActionList
import io.github.viniciuslrangel.blokts.util.BoxConstraint
import io.github.viniciuslrangel.blokts.util.JavaFXUtils
import io.github.viniciuslrangel.blokts.util.Point
import io.github.viniciuslrangel.blokts.util.point
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Accordion
import javafx.scene.control.Label
import javafx.scene.control.TitledPane
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox

/**
 * Created by <viniciuslrangel> on 23 Dec 2018, 1:51 PM (UTC-3).
 */


sealed class BaseNodeState {
    abstract fun build(): javafx.scene.Node
    abstract fun render()
    abstract val node: Node

    abstract var offsetX: Double
    abstract var offsetY: Double

    abstract val constraint: BoxConstraint

    val size by lazy {
        constraint(
            node.prefWidth,
            Math.max(
                node.prefHeight,
                (node.pins.groupBy { it.side }.map { j -> j.value.sumByDouble { k -> k.size.y } }.max()
                    ?: 0.0) + node.topPadding + 14.0
            )
        )
    }

    abstract val pinNodes: Map<Pin<*, *>, javafx.scene.Node>

    var parent: NodeContainerState? = null

    protected fun Pin<*, *>.render(context: GraphicsContext) {
        this.render(
            context,
            (if (this@BaseNodeState is NodeContainerState) this@BaseNodeState.selectedPin?.first else parent?.selectedPin?.first) == this
        )
    }
}

data class NodeState(
    override val node: Node,
    override var offsetX: Double = 0.0,
    override var offsetY: Double = 0.0,
    override val constraint: BoxConstraint = BoxConstraint()
) : BaseNodeState() {

    private var root: AnchorPane? = null
    private var canvas: Canvas? = null

    private val pinPanel = mutableMapOf<PinSide, VBox>()
    override val pinNodes = mutableMapOf<Pin<*, *>, Canvas>()

    override fun render() {
        canvas?.render()
        pinNodes.forEach { pin, canvas ->
            val context = canvas.graphicsContext2D
            context.clearRect(0.0, 0.0, canvas.width, canvas.height)
            pin.render(context)
        }
    }

    private fun Canvas.render() {
        graphicsContext2D.clearRect(0.0, 0.0, width, height)
        node.render(graphicsContext2D, width point height)
    }

    override fun build(): javafx.scene.Node {
        val anchor = AnchorPane()
        root = anchor
        JavaFXUtils.Drag.makeDraggable(anchor, this)

        val canvas = Canvas(size.x, size.y)
        this.canvas = canvas
        JavaFXUtils.AnchorPanel.setFullAnchor(canvas, 0.0)
        anchor.children += canvas

        pinPanel.clear()
        pinNodes.clear()
        val sides = node.pins.groupBy { it.side }
        for ((side, pins) in sides) {
            val vBox = pinPanel.getOrPut(side) {
                val vBox = VBox()
                when (side) {
                    PinSide.RIGHT -> JavaFXUtils.AnchorPanel.setAnchor(vBox, right = 0.0, top = node.topPadding + 14.0)
                    PinSide.LEFT -> JavaFXUtils.AnchorPanel.setAnchor(vBox, left = 0.0, top = node.topPadding + 14.0)
                }
                anchor.children += vBox
                vBox
            }
            val data = pins.map { pin ->
                pin to setupPin(pin)
            }.toMap()
            vBox.children += data.values
            pinNodes.putAll(data)
        }

        return anchor
    }

    private fun setupPin(pin: Pin<*, *>): Canvas {
        val canvas = Canvas(pin.size.x, pin.size.y)
        pin.node = canvas
        val context = canvas.graphicsContext2D

        @Suppress("RedundantLambdaArrow")
        canvas.hoverProperty().addListener { _ ->
            context.clearRect(0.0, 0.0, canvas.width, canvas.height)
            pin.render(context)
        }

        canvas.setOnMouseDragged { it.consume() }

        canvas.setOnMouseClicked { event ->
            parent?.let { parent ->
                val selected = parent.selectedPin
                when {
                    selected == null -> {
                        parent.selectedPin = pin to canvas
                        event.consume()
                    }
                    selected.second.parent.parent != this@NodeState.root -> {
                        if (pin.attachToPin(selected.first)) {
                            parent.selectedPin = null
                            event.consume()
                        }
                    }
                }
                pin.render(context)
            }
        }

        return canvas
    }

}

data class NodeContainerState(
    override val node: NodeContainer,
    override var offsetX: Double = 0.0,
    override var offsetY: Double = 0.0,
    override val constraint: BoxConstraint = BoxConstraint(),
    val children: MutableList<BaseNodeState> = mutableListOf()
) : BaseNodeState() {

    override val pinNodes: Map<Pin<*, *>, javafx.scene.Node> = emptyMap() // TODO Implement container pins

    private val childrenNodes = mutableMapOf<BaseNodeState, javafx.scene.Node>()

    private var root: StackPane? = null
    private var nodePane: Pane? = null
    private var bgCanvas: Canvas? = null
    private var fgCanvas: Canvas? = null

    var selectedPin: Pair<Pin<*, *>, Canvas>? = null

    private var lastMousePos = Point.ZERO

    private fun GraphicsContext.drawBezier(startX: Double, startY: Double, endX: Double, endY: Double) {
        val x = endX - startX
        beginPath()
        moveTo(startX, startY)
        bezierCurveTo(
            startX + x * 0.3, startY,
            startX + x * 0.6, endY,
            startX + x, endY
        )
        stroke()
    }

    override fun render() {
        bgRender()
        childrenNodes.forEach { it.key.render() }
        fgRender()
    }

    fun bgRender() = bgCanvas?.run {
        val context = graphicsContext2D
        context.clearRect(0.0, 0.0, width, height)
        node.render(context, width point height)
        node.render(context, width point height, offsetX, offsetY)
    }

    fun fgRender() = fgCanvas?.run {
        val context = graphicsContext2D
        context.clearRect(0.0, 0.0, width, height)

        context.lineWidth = 2.0

        children.forEach { child ->
            child.pinNodes.filter { it.key is InputPin && it.key.isConnected }.forEach pinNodes@{ entry ->
                val other = (entry.key as InputPin).attachedTo!!

                val startSize = entry.key.size
                val start = entry.value.localToScene(startSize.x / 2.0, startSize.y / 2.0)

                val endSize = other.size
                val end = other.node?.localToScene(endSize.x / 2.0, endSize.y / 2.0) ?: return@pinNodes
                context.stroke = other.type.color
                context.drawBezier(start.x, start.y, end.x, end.y)
            }
        }

        val selectedPin = selectedPin
        if (selectedPin != null) {
            context.stroke = selectedPin.first.type.color
            val start = selectedPin.second.localToScene(selectedPin.second.width / 2.0, selectedPin.second.height / 2.0)
            context.drawBezier(start.x, start.y, lastMousePos.x, lastMousePos.y)
        }
    }

    private var bufferedContext: Pane? = null
    private fun contextMenu(posX: Double, posY: Double) {
        val pane: Pane
        if (bufferedContext != null) {
            pane = bufferedContext!!
            pane.childrenUnmodifiable.first().apply {
                layoutX = posX
                layoutY = posY
            }
        } else {
            pane = Pane()
            fun close() {
                root?.children?.remove(pane)
            }
            pane.setOnMouseClicked { event ->
                if (event.target == pane) {
                    close()
                }
            }
            bufferedContext = pane
            val accordion = Accordion()
            pane.children += accordion
            accordion.layoutX = posX
            accordion.layoutY = posY

            accordion.panes += GeneratedActionList.actions.toSortedMap(Comparator { o1, o2 ->
                return@Comparator when {
                    o1 == "Other" -> 1
                    o2 == "Other" -> -1
                    else -> o1.compareTo(o2)
                }
            }).map {
                TitledPane(it.key, VBox().apply {
                    children += it.value.map { entry ->
                        Label(entry.first).apply {
                            setOnMouseClicked {
                                insertChild(
                                    NodeState(
                                        GeneratedActionList.build(entry.second),
                                        accordion.layoutX,
                                        accordion.layoutY
                                    )
                                )
                                close()
                            }
                        }
                    }
                })
            }
            accordion.expandedPane = accordion.panes.lastOrNull()

        }
        root?.children?.add(pane)
    }

    override fun build(): StackPane {
        val stack = StackPane()
        root = stack
        stack.prefWidth = size.x
        stack.prefHeight = size.y

        bgCanvas = Canvas().apply {
            stack.children += this
            stack.widthProperty().addListener { _, _, widthRaw ->
                val width = widthRaw.toDouble()
                if (width >= Double.MAX_VALUE) {
                    return@addListener
                }
                this@apply.width = width
                bgRender()
            }
            stack.heightProperty().addListener { _, _, heightRaw ->
                val height = heightRaw.toDouble()
                if (height >= Double.MAX_VALUE) {
                    return@addListener
                }
                this@apply.height = height
                bgRender()
            }
        }

        Pane().apply {
            nodePane = this
            stack.children += this
            insertChild(*this@NodeContainerState.children.toTypedArray())
            JavaFXUtils.Drag.makeContainerDraggable(this, this@NodeContainerState) {
                contextMenu(it.sceneX, it.sceneY)
            }
            setOnMouseClicked { event ->
                if (event.button != MouseButton.PRIMARY) {
                    return@setOnMouseClicked
                }
                val value = selectedPin
                if(value != null) {
                    selectedPin = null
                    value.first.render(value.second.graphicsContext2D)
                    fgRender()
                }
            }
        }

        fgCanvas = Canvas().apply {
            stack.children += this
            isMouseTransparent = true
            widthProperty().bind(bgCanvas!!.widthProperty())
            heightProperty().bind(bgCanvas!!.heightProperty())
            stack.setOnMouseMoved { event ->
                lastMousePos = event.x point event.y
                fgRender()
            }
            stack.setOnMouseDragged { event ->
                lastMousePos = event.x point event.y
                fgRender()
            }
        }

        return stack
    }

    private fun insertChild(vararg node: BaseNodeState) {
        children += node
        nodePane?.children?.addAll(node.map {
            it.parent = this
            val n = it.build()
            n.layoutX = it.offsetX
            n.layoutY = it.offsetY
            childrenNodes[it] = n
            it.render()
            n
        })
        // TODO Implement checker
        // System.err.println("""
        //                WARNING added to a new parent without first removing it from its current
        //                parent. It will be automatically removed from its current parent.
        //                node=$node oldparent=${node.parent} newparent=$this
        //            """.trimIndent())
    }

}