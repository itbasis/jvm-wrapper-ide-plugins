package ru.itbasis.jvmwrapper.core.jvm

import org.apache.commons.lang3.SystemUtils
import java.nio.file.Path
import java.nio.file.Paths

fun Path.fixFromMac(): Path {
	return if (SystemUtils.IS_OS_MAC) {
		this.resolve("Home")
	} else {
		this
	}
}

fun Path.getExecutable(fileName: String): Path {
	return resolve(fileName + (if (SystemUtils.IS_OS_WINDOWS) ".exe" else ""))
}

fun String?.toJvm(system: Boolean = false): Jvm {
	return Paths.get(this).toJvm(system = system)
}

fun Path?.toJvm(system: Boolean = false): Jvm {
	return Jvm(system = system, path = this)
}