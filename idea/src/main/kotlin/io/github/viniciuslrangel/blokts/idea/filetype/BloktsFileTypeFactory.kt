package io.github.viniciuslrangel.blokts.idea.filetype

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.intellij.openapi.fileTypes.FileTypeFactory
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.vfs.VirtualFile

/**
 * Created by <viniciuslrangel> on 21 Dec 2018, 4:36 PM (UTC-3).
 */
class BloktsFileTypeFactory : FileTypeFactory() {

    companion object {
        val instance: BloktsFileTypeFactory
            get() = ApplicationManager.getApplication().getComponent(BloktsFileTypeFactory::class.java)
    }

    override fun createFileTypes(fileTypeConsumer: FileTypeConsumer) {
        fileTypeConsumer.consume(BloktsFileType)
    }

    fun isValidFile(file: VirtualFile) =
        FileTypeManager.getInstance()
                .getFileTypeByFile(file) === BloktsFileType

}