package ru.itbasis.jvmwrapper.core

import org.apache.commons.lang3.SystemUtils.IS_OS_LINUX
import org.apache.commons.lang3.SystemUtils.IS_OS_MAC

object SystemInfo {
	private val ARCH_DATA_MODEL = System.getProperty("sun.arch.data.model").toLowerCase()
	//
	val is32Bit = ARCH_DATA_MODEL == "32"

	val isSupportedOS = IS_OS_LINUX || IS_OS_MAC

	val NEW_LINE = "\n"
}
