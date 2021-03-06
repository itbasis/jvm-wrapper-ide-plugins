package ru.itbasis.jvmwrapper.core.jvm

enum class JvmVendor(val code: String) {
	OPEN_JDK("openjdk"), ORACLE("oracle");
}

internal fun String.toJvmVendor(): JvmVendor {
	val lowerCase = toLowerCase()
	return when (lowerCase) {
		JvmVendor.ORACLE.code, "oracle corporation"       -> JvmVendor.ORACLE

		JvmVendor.OPEN_JDK.code, "jetbrains s.r.o", "n/a" -> JvmVendor.OPEN_JDK

		else                                              -> if (lowerCase.startsWith(prefix = JvmVendor.OPEN_JDK.code)) {
			JvmVendor.OPEN_JDK
		} else {
			throw IllegalArgumentException("value '$this' unsupported")
		}
	}
}