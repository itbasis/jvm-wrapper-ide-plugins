package ru.itbasis.jvmwrapper.core.unarchiver

import mu.KotlinLogging
import ru.itbasis.jvmwrapper.core.ProcessStepListener
import ru.itbasis.jvmwrapper.core.step
import ru.itbasis.jvmwrapper.core.unarchiver.UnarchiverFactory.FileArchiveType.DMG
import java.io.File

class DmgUnarchiver(sourceFile: File, targetDir: File, stepListener: ProcessStepListener? = null, removeOriginal: Boolean = false) :
	AbstractUnarchiver(
		sourceFile = sourceFile,
		targetDir = targetDir,
		stepListener = stepListener,
		removeOriginal = removeOriginal,
		fileNameExtension = DMG
	) {

	private val logger = KotlinLogging.logger {}

	override fun doUnpack() {
		"Detaching a previously attached dmg archive...".step(stepListener) { detach(quiet = true).run() }

		try {
			"Attaching the dmg archive...".step(stepListener) {
				val exitCode = attach().waitFor()

				require(exitCode == 0) {
					val msg = "attach error code: $exitCode"
					logger.error { msg }
					msg
				}
				require(File(volumePath).isDirectory) {
					val msg = "$volumePath is not a directory"
					logger.error { msg }
					msg
				}
			}

			"Search for PKG file...".step(stepListener) {
				findPkgFile()
			}.let { pkgFile ->

				"Unpacking the PKG file...".step(stepListener) {
					unpackPkg(pkgFile)
				}
				"Unpacking the CPIO file...".step(stepListener) {
					unpackCpio()
				}
			}
		} finally {
			"Detaching a previously attached dmg archive...".step(stepListener) {
				detach().run()
			}
		}
	}

	override fun doFinalize() {
		tempDir.deleteRecursively()
	}

	private var runtime = Runtime.getRuntime()
	private var volumePath = "/Volumes/$sourceFileName"

	private fun unpackPkg(pkgFile: File) {
		runtime.exec(
			arrayOf(
				"xar", "-xf", pkgFile.absolutePath, "."
			), null, tempDir
		).run()
	}

	private fun unpackCpio() {
		tempDir.listFiles { _, name ->
			name?.contains("jdk")
			?: false
		}.forEach { jdkDir ->
			ProcessBuilder("cpio", "-i").redirectInput(jdkDir.resolve("Payload")).directory(tempDir).start().run()
		}
	}

	private fun attach(): Process {
		require(sourceFile.isFile) {
			val msg = "$sourceFile is not a file"
			logger.error { msg }
			msg
		}

		"attach $sourceFile to $volumePath...".step(stepListener) {
			return runtime.exec(
				arrayOf(
					CMD_HDIUTIL, "attach", sourceFile.absolutePath, "-mountpoint", volumePath
				)
			)
		}
	}

	private fun detach(quiet: Boolean = false): Process {
		"detach $volumePath...".step(stepListener) {
			return runtime.exec(arrayOf(
				CMD_HDIUTIL, "detach", volumePath, "-force"
			).apply {
				if (quiet) this.plus("-quiet")
			})
		}
	}

	private fun findPkgFile(): File {
		val pkgFile = File(volumePath).walkTopDown().find { it.extension == "pkg" }
		"found PKG file: $pkgFile".step(stepListener) {}
		return pkgFile
		       ?: throw IllegalStateException("package file not found in '$volumePath'")
	}

	companion object {
		const val CMD_HDIUTIL = "hdiutil"
	}
}
