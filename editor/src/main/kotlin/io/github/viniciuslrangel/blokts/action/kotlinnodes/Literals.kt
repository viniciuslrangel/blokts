package io.github.viniciuslrangel.blokts.action.kotlinnodes

import io.github.viniciuslrangel.blokts.action.BloktsAction
import io.github.viniciuslrangel.blokts.editor.node.basic.BaseNode
import io.github.viniciuslrangel.blokts.editor.pin.OutputPin
import io.github.viniciuslrangel.blokts.editor.pin.Pin
import io.github.viniciuslrangel.blokts.editor.pin.PinType
import io.github.viniciuslrangel.blokts.editor.pin.StandardPins
import io.github.viniciuslrangel.blokts.util.JavaFXUtils
import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox

/**
 * Created by <viniciuslrangel> on 22 Jan 2019, 11:30 AM (UTC-3).
 */
@BloktsAction(category = "Math")
class NumberLiteral : BaseNode() {

    override val title = "Number"

    override val prefHeight: Double = 110.0

    private val numbers = FXCollections.observableArrayList(
        StandardPins.NUMERIC,
        StandardPins.INTEGER,
        StandardPins.FLOAT
    )

    private var selected: PinType<out Number> = StandardPins.NUMERIC
    private var value: Number = 0.0

    private fun updateValue(text: String) {
        value = when (selected) {
            StandardPins.NUMERIC, StandardPins.FLOAT -> text.toDouble()
            StandardPins.INTEGER -> text.toInt()
            else -> throw IllegalStateException("Invalid selected pin")
        }
    }

    private var lastPin: OutputPin<out Number>? = null
    override val pins: Array<Pin<*, *>>
        get() {
            val pin = OutputPin(selected)
            if(lastPin != null && lastPin!!.attachedTo.isNotEmpty()) {
                lastPin!!.attachedTo.forEach { con ->
                    pin.attachToPin(con)
                }
            }
            lastPin = pin
            return arrayOf(lastPin!!)
        }

    override fun build(pane: AnchorPane) {
        val textField = TextField().apply {
            text = value.toString()
            setOnAction {
                try {
                    updateValue(text)
                } catch (nfe: NumberFormatException) {
                    text = value.toString()
                }
            }
        }
        pane.children.add(
            VBox().apply {
                spacing = 4.0
                maxWidth = this@NumberLiteral.prefWidth - 12.0
                JavaFXUtils.AnchorPanel.setAnchor(this, left = 4.0, bottom = 4.0)
                children.addAll(
                    textField,
                    ComboBox<PinType<out Number>>(numbers).apply {
                        selectionModel.select(selected)
                        selectionModel.selectedItemProperty().addListener { _, _, value ->
                            selected = value
                            try {
                                updateValue(textField.text)
                            } catch (nfe: NumberFormatException) {
                                this@NumberLiteral.value = 0
                                textField.text = value.toString()
                            }
                            rebuild()
                        }
                        setCellFactory {
                            object : ListCell<PinType<out Number>>() {
                                override fun updateItem(item: PinType<out Number>?, empty: Boolean) {
                                    super.updateItem(item, empty)
                                    text = item?.clazz?.simpleName
                                }

                            }
                        }
                        buttonCell = cellFactory.call(null)
                    }
                )
            }
        )
    }


}
