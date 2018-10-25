package ru.itbasis.jvmwrapper.core

enum class FileArchitecture {
	X64, X86_64, I586;

	override fun toString(): String {
		return name.toLowerCase()
	}
}
