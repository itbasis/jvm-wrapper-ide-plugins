package ru.itbasis.jvmwrapper.core.system

import java.io.File
import java.nio.file.Path

class MacSystemJvmScanner : SystemJvmScanner() {
  override fun listJvmPaths(): List<Path> {
    return File("/Library/Java/JavaVirtualMachines").listFiles { pathname -> pathname.isDirectory }.map {
      it.resolve("Contents/Home")
    }.map {
      it.toPath()
    }.toList()
  }
}