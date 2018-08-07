package ru.itbasis.jvmwrapper.core.vendor

enum class JvmVendor(val code: String) {
  ORACLE("oracle"), OPEN_JDK("openjdk");

  companion object {
    @Throws(IllegalArgumentException::class)
    fun parse(code: String): JvmVendor {
      return when (code.toLowerCase()) {
        ORACLE.code, "oracle corporation" -> ORACLE
        OPEN_JDK.code, "jetbrains s.r.o" -> OPEN_JDK
        else -> throw IllegalArgumentException("value '$code' unsupported")
      }
    }

    @Throws(IllegalArgumentException::class)
    fun runtime(): JvmVendor {
      return parse(System.getProperty("java.vm.vendor"))
    }
  }
}
