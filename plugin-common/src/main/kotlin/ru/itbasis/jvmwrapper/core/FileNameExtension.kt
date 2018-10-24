package ru.itbasis.jvmwrapper.core

enum class FileNameExtension(private val extension: kotlin.String) {
	DMG("dmg"), TAR_GZ("tar.gz"), EXE("exe"), ZIP("zip");

	fun withDot(prefix: String = ""): String =
		"$prefix.$extension"
}
