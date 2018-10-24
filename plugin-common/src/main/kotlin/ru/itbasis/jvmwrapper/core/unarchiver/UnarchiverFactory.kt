package ru.itbasis.jvmwrapper.core.unarchiver

import ru.itbasis.jvmwrapper.core.FileNameExtension.DMG
import ru.itbasis.jvmwrapper.core.FileNameExtension.TAR_GZ
import ru.itbasis.jvmwrapper.core.ProcessStepListener
import java.io.File

object UnarchiverFactory {
	fun getInstance(
		sourceFile: File, targetDir: File, stepListener: ProcessStepListener? = null, removeOriginal: Boolean = true
	): AbstractUnarchiver {
		val sourceFileName = sourceFile.name.toLowerCase()
		return when {
			sourceFileName.endsWith(DMG.withDot())    -> DmgUnarchiver(sourceFile, targetDir, stepListener, removeOriginal)
			sourceFileName.endsWith(TAR_GZ.withDot()) -> TarGzUnarchiver(
				sourceFile, targetDir, stepListener, removeOriginal
			)
			else                                      -> throw IllegalArgumentException("unsupported archive type: $sourceFile")
		}
	}
}