package ru.itbasis.jvmwrapper.core.jvm.detectors

import ru.itbasis.jvmwrapper.core.jvm.getExecutable
import java.nio.file.Path
import java.util.Properties
import java.util.jar.JarFile

object JvmVersionDetector {
	@Throws(IllegalStateException::class)
	fun detect(path: Path): String {
		path.toFile().takeUnless { it.isDirectory && it.list().isNotEmpty() }?.let {
			throw IllegalArgumentException("path not found or empty directory: $path")
		}

		path.resolve("release").toFile().takeIf { it.isFile }?.let { releaseFile ->
			val properties = Properties().apply {
				releaseFile.inputStream().use { load(it) }
			}
			return properties.getProperty("JAVA_FULL_VERSION", properties.getProperty("JAVA_VERSION")).replace("\"", "")
		}

		path.resolve("jre/lib/rt.jar").toFile().takeIf { it.isFile }?.let { rtFile ->
			JarFile(rtFile, false).use { rtJarFile ->
				return rtJarFile.manifest?.mainAttributes?.getValue("Implementation-Version")!!
			}
		}

		path.resolve("bin").getExecutable("java").toFile().takeIf { it.canExecute() }?.let { javaExec ->
			val process = ProcessBuilder(javaExec.absolutePath, "-fullversion").redirectErrorStream(true).start()
			val text = process.inputStream.use { it.reader().readText() }
			val matcher = """.*"(.*)".*""".toPattern().matcher(text)
			if (matcher.find()) {
				return matcher.group(1)
			}
		}

		throw IllegalStateException("version not detected from path: $path")
	}
}