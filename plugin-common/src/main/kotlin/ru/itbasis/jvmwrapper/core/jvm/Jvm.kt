package ru.itbasis.jvmwrapper.core.jvm

import mu.KotlinLogging
import org.apache.commons.lang3.SystemUtils.IS_OS_MAC
import org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS
import ru.itbasis.jvmwrapper.core.OsType
import ru.itbasis.jvmwrapper.core.OsType.LINUX
import ru.itbasis.jvmwrapper.core.OsType.OSX
import ru.itbasis.jvmwrapper.core.OsType.WINDOWS
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
) : Comparable<Jvm> {
	private val logger = KotlinLogging.logger {}

	val major: Int
		get() {
			val value = when {
				version.contains("u") -> version.substringBefore("u")
				version.contains("_") -> version.substringAfter("1.").substringBefore(".")
				version.contains(".") -> version.replaceFirst("^1\\.".toRegex(), "").substringBefore(".")
				else                  -> version
			}.substringBefore("-ea").substringBefore("+")
			logger.trace { "major :: $version > $value , path=$path" }
			return value.toIntOrNull()
			       ?: throw IllegalArgumentException("I can not determine the major version of JVM for '$version'")
		}

	val update: Int?
		get() {
			val value = when {
				version.contains("u") -> /* 8u172 */ version.substringAfter("u")
				version.contains("_") -> /* 1.8.0_172-b11 */ version.substringAfter("_").substringBefore("-")
				version.contains(".") -> /* 10.0.1 */ version.substringAfterLast(".")
				else                  -> null
			}?.substringBefore("+")
			logger.trace { "update :: $version > $value" }
			return value?.toIntOrNull()?.takeIf { it != 0 }
		}

	val earlyAccess: Boolean = version.contains("-ea")

	val cleanVersion: String
		get() {
			return arrayOf(major, update).filterNotNull().joinToString(if (major > 8) ".0." else "u")
		}

	val os: OsType = when {
		IS_OS_MAC     -> OSX
		IS_OS_WINDOWS -> WINDOWS
		else          -> LINUX
	}

	val osAsString: String = when {
		os == OSX && major > 8 -> "macos"
		else                   -> os.toString()
	}

	override fun toString(): String {
		return "${vendor.code}-$type-$cleanVersion"
	}

	override fun compareTo(other: Jvm): Int {
		major.compareTo(other.major).takeIf { it != 0 }?.let {
			return it
		}

		if (update != null && other.update == null) {
			return 1
		}
		if (update == null && other.update != null) {
			return -1
		}
		if (other.update == null && update == null) {
			return 0
		}
		update!!.compareTo(other.update!!).takeIf { it != 0 }?.let {
			return it
		}

		return vendor.compareTo(other.vendor)
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