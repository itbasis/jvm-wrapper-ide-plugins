package ru.itbasis.jvmwrapper.core.jvm.detectors

import mu.KotlinLogging
import ru.itbasis.jvmwrapper.core.jvm.fixFromMac
import ru.itbasis.jvmwrapper.core.jvm.getExecutable
import java.nio.file.Path
import java.nio.file.Paths

object JvmVersionDetector {
	private val logger = KotlinLogging.logger {}

	fun detect(path: String): String? =
		detect(path = Paths.get(path))

	@Throws(IllegalArgumentException::class)
	fun detect(path: Path): String {
		logger.trace { "path=$path" }
		val fixedPath = path.fixFromMac()
		logger.debug { "fixedPath=$fixedPath" }

		fixedPath.toFile().takeUnless {
			it.isDirectory && it.list().isNotEmpty()
		}?.let {
			throw IllegalArgumentException("path not found or empty directory: $fixedPath")
		}

//		fixedPath.resolve("release").toFile().takeIf {
//			it.isFile && it.length() > 0
//		}?.let { releaseFile ->
//			logger.trace { "detect from release file: $releaseFile" }
//			val properties = Properties().apply {
//				releaseFile.inputStream().use { load(it) }
//			}
//			return properties.getProperty("JAVA_FULL_VERSION", properties.getProperty("JAVA_VERSION")).replace("\"", "")
//		}
//
//		fixedPath.resolve("jre/lib/rt.jar").toFile().takeIf {
//			it.isFile
//		}?.let { rtFile ->
//			logger.trace { "detect from rt.jar: $rtFile" }
//			JarFile(rtFile, false).use { rtJarFile ->
//				return rtJarFile.manifest?.mainAttributes?.getValue("Implementation-Version")!!
//			}
//		}

		fixedPath.resolve("bin").getExecutable("java").toFile().takeIf {
			it.canExecute()
		}?.let { javaExec ->
			val processBuilder = ProcessBuilder(javaExec.absolutePath, "-fullversion")
			logger.trace { "detect from executable: ${processBuilder.command()}" }
			val process = processBuilder.redirectErrorStream(true).start()
			val text = process.inputStream.use {
				it.reader().readText()
			}
			logger.trace { "output: $text" }
			val matcher = """.*"(.*)".*""".toPattern().matcher(text)
			if (matcher.find()) {
				val match = matcher.group(1)
				logger.trace { "match: $match" }
				return match
			}
		}

		throw IllegalArgumentException("version not detected from path: $fixedPath")
	}
}