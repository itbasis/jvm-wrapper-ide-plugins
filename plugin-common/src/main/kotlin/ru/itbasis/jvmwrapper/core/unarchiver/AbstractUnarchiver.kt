package ru.itbasis.jvmwrapper.core.unarchiver

import ru.itbasis.jvmwrapper.core.ProcessStepListener
import ru.itbasis.jvmwrapper.core.step
import java.io.File
import java.util.concurrent.TimeUnit

abstract class AbstractUnarchiver(
	protected val sourceFile: File,
	private val targetDir: File,
	protected val stepListener: ProcessStepListener? = null,
	private val removeOriginal: Boolean = true,
	private val fileNameExtension: UnarchiverFactory.FileArchiveType
) {
	protected val sourceFileName: String = sourceFile.name.substringBeforeLast("." + this.fileNameExtension.extension)

	protected val tempDir: File = createTempDir(suffix = sourceFileName)

	fun unpack() {
		try {
			"Running unpack the archive: $sourceFile".step(stepListener) {
				doUnpack()
			}
			"Moving unpacked content...".step(stepListener) {
				doMovingToDest()
			}
		} finally {
			doFinalize()
		}
		if (removeOriginal) {
			"Removing source archive: ".step(stepListener) {
				sourceFile.delete()
			}
		}
	}

	protected abstract fun doUnpack()

	protected open fun doMovingToDest() {
		val unpackDir = tempDir.listFiles().first()
		val macContentsDirectory = unpackDir.resolve("Contents")
		if (macContentsDirectory.isDirectory) {
			macContentsDirectory.renameTo(targetDir)
		} else {
			unpackDir.renameTo(targetDir)
		}
	}

	protected open fun doFinalize() {}

	protected fun Process.run() {
		this.waitFor(10, TimeUnit.SECONDS)
//    process.inputStream.copyTo(System.out)
//    process.errorStream.copyTo(System.out)
	}
}

