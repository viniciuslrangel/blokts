package io.github.viniciuslrangel.blokts.action

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.File
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

/**
 * Created by <viniciuslrangel> on 25 Dec 2018, 8:53 PM (UTC-3).
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class BloktsAction(val name: String = "", val category: String = "")

private const val KAPT_KOTLIN_GENERATED = "kapt.kotlin.generated"

//@AutoService(Processor::class)
@SupportedAnnotationTypes("io.github.viniciuslrangel.blokts.action.BloktsAction")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(KAPT_KOTLIN_GENERATED)
class ActionProcessor : AbstractProcessor() {

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (annotations.any {
                !processingEnv.typeUtils.isSameType(
                    it.asType(),
                    processingEnv.elementUtils.getTypeElement(BloktsAction::class.java.canonicalName).asType()
                )
            }) {
            return false
        }

        val genDir = processingEnv.options[KAPT_KOTLIN_GENERATED] ?: return false.also {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "cant find target directory for generated files")
        }

        val elements = roundEnv.getElementsAnnotatedWith(BloktsAction::class.java)
        if (elements.any { it.kind != ElementKind.CLASS }) {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "invalid annotation kind (non-class)")
            return false
        }

        var lastId = 0

        val actionMap = ClassName("kotlin.collections", "Map").parameterizedBy(
            ClassName("kotlin", "String"),
            ClassName("kotlin.collections", "List").parameterizedBy(
                ClassName("kotlin", "Pair").parameterizedBy(
                    ClassName("kotlin", "String"),
                    ClassName("kotlin", "Int")
                )
            )
        )
        val listOfElement = LinkedList<Element>()

        val mapCode = CodeBlock.builder()
        mapCode.addStatement("mapOf(")
        mapCode.indent()

        var first = true
        elements.map {
            it to it.getAnnotation(BloktsAction::class.java)
        }.groupBy { (_, annotation) ->
            annotation.category.ifBlank { "Other" }
        }.forEach { (key, elements) ->
            val block = CodeBlock.builder()
            block.addStatement("listOf(")
            block.indent()
            elements.forEachIndexed { index, (it, annotation) ->
                val name = annotation.name.ifBlank { it.simpleName }
                block.addStatement("%S to %L%L", name, lastId, if (index != elements.lastIndex) "," else "")
                listOfElement.add(lastId++, it)
            }
            block.unindent()
            block.addStatement(")")

            if (first) {
                first = false
            } else {
                mapCode.add(",\n")
            }
            mapCode.add("%S to ", key)
            mapCode.add(block.build())
        }

        mapCode.unindent()
        mapCode.addStatement(")")

        val objBuilt = TypeSpec.objectBuilder("GeneratedActionList")
            .addProperty(
                PropertySpec
                    .builder("actions", actionMap)
                    .mutable(false)
                    .initializer(
                        mapCode.build().toString()
                    )
                    .build()
            )
            .addFunction(
                FunSpec.builder("build")
                    .addParameter("id", Int::class)
                    .returns(ClassName("io.github.viniciuslrangel.blokts.editor.node", "Node"))
                    .addCode(
                        CodeBlock.builder().apply {
                            beginControlFlow("return when(id)")
                            listOfElement.forEachIndexed { index, element ->
                                addStatement("%L -> %T()", index, element.asType())
                            }
                            addStatement("else -> throw IllegalArgumentException(%S)", "invalid node id")
                            endControlFlow()
                        }.build()
                    )
                    .build()
            )
            .build()
        FileSpec.builder("io.github.viniciuslrangel.blokts.generated", objBuilt.name!!)
            .addType(objBuilt)
            .build().writeTo(File(genDir))

        return true
    }

}