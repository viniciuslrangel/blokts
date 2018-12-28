package io.github.viniciuslrangel.blokts.idea.filetype

import com.intellij.openapi.fileTypes.LanguageFileType
import io.github.viniciuslrangel.blokts.idea.BloktsIcons
import io.github.viniciuslrangel.blokts.idea.BloktsMap

/**
 * Created by <viniciuslrangel> on 21 Dec 2018, 4:37 PM (UTC-3).
 */
object BloktsFileType : LanguageFileType(BloktsMap) {

    override fun getName() = "Blokts"

    override fun getIcon() = BloktsIcons.FILE

    override fun getDefaultExtension() = "bkts"

    override fun getDescription() = "Blokts map file"
}