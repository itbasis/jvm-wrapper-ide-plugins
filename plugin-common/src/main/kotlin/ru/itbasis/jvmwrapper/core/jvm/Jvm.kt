package ru.itbasis.jvmwrapper.core.jvm

import org.apache.commons.lang3.SystemUtils.IS_OS_MAC
import org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS
import ru.itbasis.jvmwrapper.core.jvm.JvmType.Companion.detectJvmType
import ru.itbasis.jvmwrapper.core.vendor.JvmVendor
import java.nio.file.Path
import java.util.Properties
import java.util.jar.JarFile

data class Jvm(
  val system: Boolean = false,
  val path: Path? = null,
  val version: String = detectJvmVersion(path!!),
  val type: JvmType = detectJvmType(path!!),
  val vendor: JvmVendor
) {

  val major: String
    get() {
      return version.substringBefore("_").substringBefore("u").substringAfter("1.").substringBefore(".")
    }

  val update: String?
    get() {
      return if (version.contains("u") or version.contains("_")) version.substringAfter("_").substringAfter("u").substringAfterLast(".").substringBefore(
        "-"
      ) else null
    }

  val cleanVersion: String = if (update == null) version.substringBefore("+") else "${major}u$update"

  val os: String
    get() {
      return when {
        IS_OS_MAC -> if (major.toInt() > 8) "osx" else "macosx"
        IS_OS_WINDOWS -> "windows"
        else -> "linux"
      }
    }

  override fun toString(): String {
    return "${vendor.code}-$type-$cleanVersion"
  }

  companion object {
    private fun detectJvmVersion(path: Path): String {
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
}
