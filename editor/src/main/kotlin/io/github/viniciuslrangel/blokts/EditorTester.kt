package io.github.viniciuslrangel.blokts

import io.github.viniciuslrangel.blokts.editor.Renderer
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

/**
 * Created by <viniciuslrangel> on 6/17/2018, 12:14 AM (UTC-3).
 */

class EditorTester : Application() {

    val renderer = Renderer()/*EditorRenderer(RootNodeContainer().apply {
        attachChild(LabeledNode("Test"))
    })*/

    override fun start(primaryStage: Stage) {
        primaryStage.title = "Testing"
        primaryStage.width = 800.0
        primaryStage.height = 600.0
        primaryStage.scene = Scene(renderer.build())
        primaryStage.show()
        renderer.render()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Application.launch(EditorTester::class.java, *args)
        }
    }

}