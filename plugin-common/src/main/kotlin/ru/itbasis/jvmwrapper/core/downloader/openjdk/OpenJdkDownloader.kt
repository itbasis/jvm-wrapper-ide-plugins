package ru.itbasis.jvmwrapper.core.downloader.openjdk

import mu.KotlinLogging
import ru.itbasis.jvmwrapper.core.FileNameExtension
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

		val htmlContent = this!!.htmlContent(httpClient = httpClient)
		val remoteFileUrl = regexDownloadFile.findOne(htmlContent)
		                    ?: return null
		val remoteFileChecksumUrl = "$remoteFileUrl.sha256"
		val remoteFileChecksum =
			if (htmlContent.contains(remoteFileChecksumUrl)) remoteFileChecksumUrl.htmlContent(httpClient = httpClient) else null
		return RemoteArchiveFile(url = remoteFileUrl, checksum = remoteFileChecksum, archiveFileExtension = archiveFileExtension)
	}

	override val archiveFileExtension = when {
		jvm.major >= 9 -> FileNameExtension.TAR_GZ
		else           -> super.archiveFileExtension
	}

	private val regexDownloadFile: Regex by lazy {
		val filename = archiveFileExtension.withDot(prefix = "jdk-${jvm.major}.*?${jvm.os}-$archiveArchitecture-.*?")
		return@lazy """"(https://download.java.net/java/.*?$filename)"""".toRegex(IGNORE_CASE)
	}
}
