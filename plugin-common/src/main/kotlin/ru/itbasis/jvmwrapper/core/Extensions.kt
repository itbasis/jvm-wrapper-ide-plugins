package ru.itbasis.jvmwrapper.core

import org.apache.commons.lang3.SystemUtils.IS_OS_MAC
import org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS
import java.io.File

internal fun File.extension(): String {
  return when {
    IS_OS_MAC -> "dmg"
    IS_OS_WINDOWS -> "exe"
    else -> "tar.gz"
  }
}

internal fun File.archiveNameWithoutExtension(): String {
  return this.name.substringBefore("." + extension())
}
