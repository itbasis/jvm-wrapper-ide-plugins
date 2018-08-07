package ru.itbasis.jvmwrapper.core

object SystemInfo {
  private val ARCH_DATA_MODEL = System.getProperty("sun.arch.data.model").toLowerCase()
  //
  val is32Bit = ARCH_DATA_MODEL == "32"
}
