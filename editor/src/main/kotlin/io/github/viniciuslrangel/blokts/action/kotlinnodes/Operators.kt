package io.github.viniciuslrangel.blokts.action.kotlinnodes

import io.github.viniciuslrangel.blokts.action.BloktsAction
import io.github.viniciuslrangel.blokts.editor.node.basic.BaseNode
import io.github.viniciuslrangel.blokts.editor.pin.InputPin
import io.github.viniciuslrangel.blokts.editor.pin.OutputPin
import io.github.viniciuslrangel.blokts.editor.pin.Pin
import io.github.viniciuslrangel.blokts.editor.pin.StandardPins

/**
 * Created by <viniciuslrangel> on 25 Dec 2018, 9:01 PM (UTC-3).
 */
@BloktsAction
class Add : BaseNode() {

    override val title = "Add"

    override val pins: Array<Pin<*, *>> = arrayOf(
        InputPin(StandardPins.BOOLEAN),
        InputPin(StandardPins.FLOAT),
        InputPin(StandardPins.INTEGER),
        OutputPin(StandardPins.FLOAT),
        OutputPin(StandardPins.BOOLEAN)
    )

}
