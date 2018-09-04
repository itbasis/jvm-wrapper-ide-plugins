package ru.itbasis.jvmwrapper.core

enum class JvmType {
  JVM, JDK;

  override fun toString() = this.name.toLowerCase()
}

internal fun String.toJvmType(): JvmType {
  return JvmType.valueOf(this.toUpperCase())
}