package ru.itbasis.jvmwrapper.core.unarchiver

import ru.itbasis.jvmwrapper.core.ProcessStepListener
import java.io.File

object UnarchiverFactory {
	fun getInstance(
		sourceFile: File, targetDir: File , stepListener: ProcessStepListener? = null, removeOriginal: Boolean = false
	): AbstractUnarchiver {
		return when {
			sourceFile.name.endsWith("." + FileArchiveType.DMG.extension) -> DmgUnarchiver(sourceFile, targetDir, stepListener, removeOriginal)
			sourceFile.name.endsWith("." + FileArchiveType.TAR_GZ.extension) -> TarGzUnarchiver(
				sourceFile,
				targetDir,
				stepListener,
				removeOriginal
			)
			else -> throw IllegalArgumentException("unsupported archive type: $sourceFile")
		}
	}

	enum class FileArchiveType(val extension: kotlin.String) {
		DMG("dmg"), TAR_GZ("tar.gz"), EXE("exe")
	}
}