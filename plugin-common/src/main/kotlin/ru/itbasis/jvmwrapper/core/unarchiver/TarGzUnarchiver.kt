package ru.itbasis.jvmwrapper.core.unarchiver

import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver
import org.codehaus.plexus.logging.slf4j.Slf4jLoggerManager
import ru.itbasis.jvmwrapper.core.ProcessStepListener
import ru.itbasis.jvmwrapper.core.unarchiver.UnarchiverFactory.FileArchiveType.TAR_GZ
import java.io.File

class TarGzUnarchiver(sourceFile: File, targetDir: File, stepListener: ProcessStepListener? = null, removeOriginal: Boolean = false) :
	AbstractUnarchiver(
		sourceFile = sourceFile,
		targetDir = targetDir,
		stepListener = stepListener,
		removeOriginal = removeOriginal,
		fileNameExtension = TAR_GZ
	) {

	private val unpackLogger = Slf4jLoggerManager().run {
		this.initialize()
		return@run this.getLoggerForComponent(this::javaClass.name)
	}!!

	override fun doUnpack() {
		val unArchiver = TarGZipUnArchiver(sourceFile)
		unArchiver.enableLogging(unpackLogger)
		unArchiver.destDirectory = tempDir
		unArchiver.extract()
	}
}
