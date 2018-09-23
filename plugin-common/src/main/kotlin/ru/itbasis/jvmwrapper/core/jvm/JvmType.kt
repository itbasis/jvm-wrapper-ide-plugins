package ru.itbasis.jvmwrapper.core.jvm

enum class JvmType {
	JRE, JDK;

	override fun toString() =
		this.name.toLowerCase()
}

internal fun String.toJvmType(): JvmType {
	return JvmType.valueOf(this.toUpperCase())
}