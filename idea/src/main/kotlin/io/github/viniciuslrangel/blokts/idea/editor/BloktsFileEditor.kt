package io.github.viniciuslrangel.blokts.idea.editor

import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.openapi.Disposable
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.UserDataHolderBase
import org.jetbrains.kotlin.psi.KtFile
import java.beans.PropertyChangeListener
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Created by <viniciuslrangel> on 21 Dec 2018, 4:55 PM (UTC-3).
 */
class BloktsFileEditor(val project: Project, val ktFile: KtFile) : UserDataHolderBase(), FileEditor, Disposable {

    //private val editor by lazy { BloktsEditorPanel(project, ktFile) }

    override fun isModified(): Boolean {
        return true // TODO What this does?
    }

    override fun getName() = "Blokts"

    override fun isValid() = ktFile.isValid

    override fun getPreferredFocusedComponent(): JComponent? {
        return null
    }

    override fun getComponent(): JComponent = JPanel()//editor.component

    override fun dispose() = Unit

    override fun getCurrentLocation(): FileEditorLocation? = null

    override fun getBackgroundHighlighter(): BackgroundEditorHighlighter? = null

    override fun selectNotify() = Unit

    override fun deselectNotify() = Unit

    override fun setState(state: FileEditorState) = Unit

    override fun removePropertyChangeListener(listener: PropertyChangeListener) = Unit

    override fun addPropertyChangeListener(listener: PropertyChangeListener) = Unit

}