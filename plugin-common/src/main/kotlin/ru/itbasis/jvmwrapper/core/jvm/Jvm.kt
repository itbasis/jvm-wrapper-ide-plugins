package ru.itbasis.jvmwrapper.core.jvm

import org.apache.commons.lang3.SystemUtils.IS_OS_MAC
import org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS
import ru.itbasis.jvmwrapper.core.jvm.JvmVendor.OPEN_JDK
import ru.itbasis.jvmwrapper.core.jvm.detectors.JvmTypeDetector
import ru.itbasis.jvmwrapper.core.jvm.detectors.JvmVendorDetector
import ru.itbasis.jvmwrapper.core.jvm.detectors.JvmVersionDetector
import ru.itbasis.jvmwrapper.core.unarchiver.UnarchiverFactory.FileArchiveType.DMG
import ru.itbasis.jvmwrapper.core.unarchiver.UnarchiverFactory.FileArchiveType.EXE
import ru.itbasis.jvmwrapper.core.unarchiver.UnarchiverFactory.FileArchiveType.TAR_GZ
import ru.itbasis.jvmwrapper.core.unarchiver.UnarchiverFactory.FileArchiveType.ZIP
import java.nio.file.Path

data class Jvm(
	val system: Boolean = false,
	val path: Path? = null,
	val version: String = JvmVersionDetector.detect(path!!.fixFromMac()),
	val type: JvmType = JvmTypeDetector.detect(path!!.fixFromMac()),
	val vendor: JvmVendor = JvmVendorDetector.detect(path!!.fixFromMac())
) : Comparable<Jvm> {

	val major: Int
		get() {
			val value = when {
				version.contains("u") -> version.substringBefore("u")
				version.contains("_") -> version.substringAfter("1.").substringBefore(".")
				version.contains(".") -> version.replaceFirst("^1\\.".toRegex(), "").substringBefore(".")
				else                  -> version
			}.substringBefore("-ea").substringBefore("+")
			println("major :: $version > $value")
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
			println("update :: $version > $value")
			return value?.toIntOrNull()?.takeIf { it != 0 }
		}

	val earlyAccess: Boolean = version.contains("-ea")

	val cleanVersion: String
		get() {
			return arrayOf(major, update).filterNotNull().joinToString(if (major > 8) ".0." else "u")
		}

	val os: String
		get() {
			return when {
				IS_OS_MAC     -> if (major > 8) "osx" else "macosx"
				IS_OS_WINDOWS -> "windows"
				else          -> "linux"
			}
		}

	val archiveFileExtension: String by lazy {
		when {
			major >= 11 && IS_OS_WINDOWS     -> ZIP
			major >= 9 && vendor == OPEN_JDK -> TAR_GZ

			IS_OS_MAC                        -> DMG
			IS_OS_WINDOWS                    -> EXE

			else                             -> TAR_GZ
		}.extension
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