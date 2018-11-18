package ru.itbasis.jvmwrapper.core.downloader.openjdk

import mu.KotlinLogging
import org.apache.commons.lang3.SystemUtils
import ru.itbasis.jvmwrapper.core.FileArchitecture.X64
import ru.itbasis.jvmwrapper.core.FileNameExtension
import ru.itbasis.jvmwrapper.core.SystemInfo.is32Bit
import ru.itbasis.jvmwrapper.core.downloader.AbstractDownloader
import ru.itbasis.jvmwrapper.core.downloader.RemoteArchiveFile
import ru.itbasis.jvmwrapper.core.findOne
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import kotlin.text.RegexOption.IGNORE_CASE

class OpenJdkDownloader(jvm: Jvm) : AbstractDownloader(jvm = jvm) {
	override val logger = KotlinLogging.logger {}

	override val remoteArchiveFile: RemoteArchiveFile by lazy {
		"https://jdk.java.net/${jvm.major}/".getRemoteArchiveFile()
		?: throw IllegalStateException("it was not succeeded to define URL for version $jvm")
	}

	override fun String?.getRemoteArchiveFile(): RemoteArchiveFile? {
		require(!this.isNullOrBlank()) {
			val msg = "request url null or empty"
			logger.error { msg }
			return@require msg
		}

		val htmlContent = this.htmlContent(httpClient = httpClient)
		val remoteFileUrl = regexDownloadFile.findOne(htmlContent)
		                    ?: return null
		val remoteFileChecksumUrl = "$remoteFileUrl.sha256"
		val remoteFileChecksum =
			if (htmlContent.contains(remoteFileChecksumUrl)) remoteFileChecksumUrl.htmlContent(httpClient = httpClient) else null
		return RemoteArchiveFile(url = remoteFileUrl, checksum = remoteFileChecksum, archiveFileExtension = archiveFileExtension)
	}

	override val archiveFileExtension = when {
		jvm.major >= 9 -> when {
			SystemUtils.IS_OS_WINDOWS -> FileNameExtension.ZIP
			else                      -> FileNameExtension.TAR_GZ
		}
		else           -> super.archiveFileExtension
	}

	override val archiveArchitecture = when {
		jvm.major >= 10 && !is32Bit -> X64
		else                        -> super.archiveArchitecture
	}

	private val regexDownloadFile: Regex by lazy {
		val filename = archiveFileExtension.withDot(prefix = "jdk-${jvm.major}.*?${jvm.osAsString}-$archiveArchitecture.*?")
		val regexp = """"(https://download.java.net/java/.*?$filename)""""
		logger.debug { "regexp: $regexp" }
		return@lazy regexp.toRegex(IGNORE_CASE)
	}
}
