package ru.itbasis.jvmwrapper.core.jvm.detectors

import ru.itbasis.jvmwrapper.core.jvm.JvmType
import ru.itbasis.jvmwrapper.core.jvm.getExecutable
import java.nio.file.Path

object JvmTypeDetector {
	@Throws(IllegalStateException::class)
	fun detect(path: Path): JvmType {
		val binPath = path.resolve("bin")
		return when {
			binPath.isExecutableFile("javac") -> JvmType.JDK
			binPath.isExecutableFile("java")  -> JvmType.JRE
			else                              -> throw IllegalStateException("jvm type not detected from path: $path")
		}
	}

	private fun Path.isExecutableFile(fileName: String): Boolean {
		val file = getExecutable(fileName).toFile()
		return file.isFile && file.canExecute()
	}
}