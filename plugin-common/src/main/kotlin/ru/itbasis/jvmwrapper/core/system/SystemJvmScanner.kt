package ru.itbasis.jvmwrapper.core.system

import org.apache.commons.lang3.SystemUtils.IS_OS_MAC
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.jvmwrapper.core.vendor.JvmVendor
import java.nio.file.Path

abstract class SystemJvmScanner {
  internal abstract fun listJvmPaths(): List<Path>

  fun listJvm(): List<Jvm> {
    return listJvmPaths().map {
      Jvm(system = true, vendor = JvmVendor.ORACLE, path = it)
    }
  }

  companion object {
    fun getInstance(): SystemJvmScanner {
      if (IS_OS_MAC) return MacSystemJvmScanner()
      return dummy
    }

    @Deprecated(message = "Delete as soon as all OS support is added")
    private val dummy = object : SystemJvmScanner() {
      override fun listJvmPaths(): List<Path> = emptyList()
    }
  }
}
