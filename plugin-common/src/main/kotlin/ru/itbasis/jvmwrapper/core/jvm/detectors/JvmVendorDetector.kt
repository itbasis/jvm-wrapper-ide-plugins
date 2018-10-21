package ru.itbasis.jvmwrapper.core.jvm.detectors

import ru.itbasis.jvmwrapper.core.jvm.JvmVendor
import ru.itbasis.jvmwrapper.core.jvm.fixFromMac
import ru.itbasis.jvmwrapper.core.jvm.getExecutable
import ru.itbasis.jvmwrapper.core.jvm.toJvmVendor
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Properties
import java.util.jar.JarFile

object JvmVendorDetector {
	fun detect(path: String): JvmVendor =
		detect(path = Paths.get(path))

	fun detect(path: Path): JvmVendor {
		val fixedPath = path.fixFromMac()

		fixedPath.resolve("bin").getExecutable("java").toFile().takeIf { it.canExecute() }?.let { javaExec ->
			val process = ProcessBuilder(javaExec.absolutePath, "-fullversion").redirectErrorStream(true).start()
			val text = process.inputStream.use { it.reader().readText() }
			try {
				return text.toJvmVendor()
			} catch (ignored: Exception) {
			}
		}

		fixedPath.resolve("release").toFile().takeIf { it.isFile }?.let { releaseFile ->
			val properties = Properties().apply {
				releaseFile.inputStream().use {
					load(it)
				}
			}
			properties.getProperty("IMPLEMENTOR")?.let {
				return it.replace("\"", "").toJvmVendor()
			}
		}

		fixedPath.resolve("jre/lib/rt.jar").toFile().takeIf { it.isFile }?.let { rtFile ->
			JarFile(rtFile, false).use { rtJarFile ->
				val mainAttributes = rtJarFile.manifest?.mainAttributes!!
				(mainAttributes.getValue("Implementation-Vendor")
				 ?: mainAttributes.getValue("Implementation-Version"))?.let {
					return it.toJvmVendor()
				}
			}
		}

		throw IllegalStateException("vendor not detected from path: $fixedPath")
	}
}
