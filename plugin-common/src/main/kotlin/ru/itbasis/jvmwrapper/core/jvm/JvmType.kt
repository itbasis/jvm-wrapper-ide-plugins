package ru.itbasis.jvmwrapper.core.jvm

import java.nio.file.Path

enum class JvmType {
  JRE, JDK;

  override fun toString() = this.name.toLowerCase()

  companion object {
    fun detectJvmType(path: Path): JvmType {
      return if (path.resolve("bin").getExecutable("javac").toFile().isFile) JDK else JRE
    }
  }
}

internal fun String.toJvmType(): JvmType {
  return JvmType.valueOf(this.toUpperCase())
}