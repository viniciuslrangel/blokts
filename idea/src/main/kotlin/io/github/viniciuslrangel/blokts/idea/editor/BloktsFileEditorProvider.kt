package io.github.viniciuslrangel.blokts.idea.editor

import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import io.github.viniciuslrangel.blokts.idea.filetype.BloktsFileTypeFactory
import org.jetbrains.kotlin.psi.KtFile

/**
 * Created by <viniciuslrangel> on 21 Dec 2018, 4:45 PM (UTC-3).
 */
class BloktsFileEditorProvider(val fileTypeFactory: BloktsFileTypeFactory = BloktsFileTypeFactory.instance) : FileEditorProvider {

    companion object {
        val EDITOR_ID = "BloktsEditor"
    }

    override fun accept(project: Project, file: VirtualFile): Boolean {
        if (!fileTypeFactory.isValidFile(file))
            return false
        return PsiManager
                .getInstance(project)
                .findFile(file)
                ?.containingDirectory
                ?.findFile(file.nameWithoutExtension + ".kt") != null
    }

    override fun getEditorTypeId() = EDITOR_ID

    override fun getPolicy() =
            FileEditorPolicy.HIDE_DEFAULT_EDITOR

    override fun createEditor(project: Project, file: VirtualFile) =
            BloktsFileEditor(project,
                    PsiManager
                            .getInstance(project)
                            .findFile(file)!!
                            .containingDirectory
                            .findFile(file.nameWithoutExtension + ".kt") as KtFile
            )

}