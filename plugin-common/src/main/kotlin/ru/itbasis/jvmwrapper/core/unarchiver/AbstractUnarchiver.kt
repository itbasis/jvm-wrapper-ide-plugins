package ru.itbasis.jvmwrapper.core.unarchiver

import ru.itbasis.jvmwrapper.core.ProcessStepListener
import ru.itbasis.jvmwrapper.core.step
import java.io.File
import java.util.concurrent.TimeUnit

abstract class AbstractUnarchiver(
	protected val sourceFile: File,
	protected val targetDir: File,
	protected val stepListener: ProcessStepListener? = null,
	private val removeOriginal: Boolean = true
) {
	protected abstract val sourceFileName: String

	protected val tempDir by lazy { createTempDir(suffix = sourceFileName) }

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
	protected abstract fun doMovingToDest()
	protected open fun doFinalize() {}

	protected fun Process.run() {
		this.waitFor(10, TimeUnit.SECONDS)
//    process.inputStream.copyTo(System.out)
//    process.errorStream.copyTo(System.out)
	}
}

