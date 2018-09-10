package ru.itbasis.jvmwrapper.core.vendor

enum class JvmVendor(val code: String) {
  ORACLE("oracle"), OPEN_JDK("openjdk");
}

internal fun String.toJvmVendor(): JvmVendor {
  return when (toLowerCase()) {
    JvmVendor.ORACLE.code, "oracle corporation" -> JvmVendor.ORACLE
    JvmVendor.OPEN_JDK.code, "jetbrains s.r.o" -> JvmVendor.OPEN_JDK
    else -> throw IllegalArgumentException("value '$this' unsupported")
  }
}