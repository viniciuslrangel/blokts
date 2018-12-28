package io.github.viniciuslrangel.blokts.idea.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.psi.PsiFileFactory
import io.github.viniciuslrangel.blokts.idea.filetype.BloktsFileType
import org.jetbrains.kotlin.idea.util.application.runWriteAction
import org.jetbrains.kotlin.psi.KtFile

/**
 * Created by <viniciuslrangel> on 21 Dec 2018, 3:29 PM (UTC-3).
 */
class GenerateBloktsFile : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        e.getData(PlatformDataKeys.SELECTED_ITEMS)?.filterIsInstance<KtFile>()?.forEach { ktFile ->
            val dir = ktFile.containingDirectory ?: return@forEach
            val fileName = ktFile.name.removeSuffix(".kt") + ".bkts"
            if (dir.findFile(fileName) != null)
                return
            runWriteAction {
                dir.add(
                        PsiFileFactory.getInstance(ktFile.project).createFileFromText(
                                fileName,
                                BloktsFileType,
                                "{}"
                        )
                )

            }
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible =
                e.getData(PlatformDataKeys.SELECTED_ITEMS)?.all {
                    it is KtFile && !it.isScript()
                } ?: false
    }

}
