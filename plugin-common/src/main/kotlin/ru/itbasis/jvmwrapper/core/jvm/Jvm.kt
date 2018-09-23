package ru.itbasis.jvmwrapper.core.jvm

import org.apache.commons.lang3.SystemUtils.IS_OS_MAC
import org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS
import ru.itbasis.jvmwrapper.core.jvm.JvmVendor.OPEN_JDK
import ru.itbasis.jvmwrapper.core.jvm.detectors.JvmTypeDetector
import ru.itbasis.jvmwrapper.core.jvm.detectors.JvmVendorDetector
import ru.itbasis.jvmwrapper.core.jvm.detectors.JvmVersionDetector
import java.nio.file.Path

data class Jvm(
	val system: Boolean = false,
	val path: Path? = null,
	val version: String = JvmVersionDetector.detect(path!!),
	val type: JvmType = JvmTypeDetector.detect(path!!),
	val vendor: JvmVendor = JvmVendorDetector.detect(path!!)
) {
	val major: String
		get() {
			return version.substringBefore("_").substringBefore("u").substringAfter("1.").substringBefore(".")
		}

	val update: String?
		get() {
			return if (version.contains("u") or version.contains("_")) version.substringAfter("_").substringAfter("u").substringAfterLast(".").substringBefore(
				"-"
			) else null
		}

	val cleanVersion: String = if (update == null) version.substringBefore("+") else "${major}u$update"

	val os: String
		get() {
			return when {
				IS_OS_MAC     -> if (major.toInt() > 8) "osx" else "macosx"
				IS_OS_WINDOWS -> "windows"
				else          -> "linux"
			}
		}

	override fun toString(): String {
		return when (vendor) {
			OPEN_JDK -> "${vendor.code}-$cleanVersion"
			else     -> "${vendor.code}-$type-$cleanVersion"
		}
	}

	companion object {
		fun adjustPath(path: Path?): Path? {
			return arrayOf(path?.resolve("Home"), path?.resolve("Contents/Home"), path).firstOrNull {
				it?.toFile()?.isDirectory
				?: false
			}
			       ?: return null
		}
	}
}
