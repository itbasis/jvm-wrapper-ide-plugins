package ru.itbasis.jvmwrapper.core

import org.apache.commons.lang3.SystemUtils.IS_OS_LINUX
import org.apache.commons.lang3.SystemUtils.IS_OS_MAC
import org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS

enum class OsType {
	OSX, LINUX, WINDOWS, ALPINE_LINUX;

	override fun toString(): String {
		return name.toLowerCase()
	}

	companion object {
		fun currentOs(): OsType =
			when {
				IS_OS_MAC     -> OSX
				IS_OS_LINUX   -> LINUX
				IS_OS_WINDOWS -> WINDOWS
				else          -> throw IllegalStateException("Unsupported OS")
			}
	}
}
