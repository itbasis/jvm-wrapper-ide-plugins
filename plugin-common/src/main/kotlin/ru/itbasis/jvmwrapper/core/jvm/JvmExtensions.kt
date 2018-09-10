package ru.itbasis.jvmwrapper.core.jvm

import org.apache.commons.lang3.SystemUtils
import java.nio.file.Path

fun Path.getExecutable(fileName: String): Path {
  return resolve(fileName + (if (SystemUtils.IS_OS_WINDOWS) ".exe" else ""))
}