package ru.itbasis.jvmwrapper.core

import org.apache.commons.lang3.SystemUtils.IS_OS_MAC
import org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS

data class JvmVersion(val type: JvmType = JvmType.JDK, val version: String) {
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

  val os: String
    get() {
      return when {
        IS_OS_MAC -> if (major.toInt() > 8) "osx" else "macosx"
        IS_OS_WINDOWS -> "windows"
        else -> "linux"
      }
    }

  val cleanVersion: String = if (update == null) version.substringBefore("+") else "${major}u$update"

  override fun toString(): String = "JvmVersion($type-$version)"

  companion object {
    fun runtime(): JvmVersion {
      return JvmVersion(version = System.getProperty("java.version"))
    }
  }
}
