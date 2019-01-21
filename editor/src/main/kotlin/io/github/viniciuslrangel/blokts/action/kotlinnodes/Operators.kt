package io.github.viniciuslrangel.blokts.action.kotlinnodes

import io.github.viniciuslrangel.blokts.action.BloktsAction
import io.github.viniciuslrangel.blokts.editor.node.basic.BaseNode
import io.github.viniciuslrangel.blokts.editor.pin.InputPin
import io.github.viniciuslrangel.blokts.editor.pin.OutputPin
import io.github.viniciuslrangel.blokts.editor.pin.Pin
import io.github.viniciuslrangel.blokts.editor.pin.StandardPins
import io.github.viniciuslrangel.blokts.util.JavaFXUtils
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.scene.text.Font

/**
 * Created by <viniciuslrangel> on 25 Dec 2018, 9:01 PM (UTC-3).
 */
@BloktsAction
class Add : BaseNode() {

    override val title = "Add"

    override var pins: Array<Pin<*, *>> = arrayOf(
        InputPin(StandardPins.NUMERIC),
        InputPin(StandardPins.NUMERIC),
        OutputPin(StandardPins.NUMERIC)
    )

    override fun build(pane: AnchorPane) {
        pane.children.add(Label("+").apply{
            font = Font.font(16.0)
            JavaFXUtils.AnchorPanel.setAnchor(this, left = 32.0, bottom = 0.0)
            setOnMouseClicked {
                pins += InputPin(StandardPins.NUMERIC)
                rebuild()
            }
        })
    }



}
