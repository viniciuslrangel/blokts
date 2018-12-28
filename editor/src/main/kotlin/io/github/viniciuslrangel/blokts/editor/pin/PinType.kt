package io.github.viniciuslrangel.blokts.editor.pin

import javafx.scene.paint.Color
import kotlin.reflect.KClass

/**
 * Created by <viniciuslrangel> on 23 Dec 2018, 12:53 PM (UTC-3).
 */
data class PinType<T : Any> constructor(val clazz: KClass<T>, val color: Color = Color.GRAY)