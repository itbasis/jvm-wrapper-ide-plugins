package ru.itbasis.jvmwrapper.core.downloader

import ru.itbasis.jvmwrapper.core.downloader.openjdk.OpenJdkDownloader
import ru.itbasis.jvmwrapper.core.downloader.oracle.OracleDownloader
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.jvmwrapper.core.jvm.JvmVendor

object DownloaderFactory {
	@JvmStatic
	fun getInstance(jvm: Jvm): AbstractDownloader {
		@Suppress("REDUNDANT_ELSE_IN_WHEN") return when (jvm.vendor) {
			JvmVendor.OPEN_JDK -> OpenJdkDownloader(jvm = jvm)
			JvmVendor.ORACLE   -> OracleDownloader(jvm = jvm)

			else               -> throw IllegalArgumentException("Unsupported jvm vendor: ${jvm.vendor}")
		}
	}
}