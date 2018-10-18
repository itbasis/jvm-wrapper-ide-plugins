package ru.itbasis.jvmwrapper.core.downloader

import mu.KLogger
import ru.itbasis.jvmwrapper.core.HttpClient
import ru.itbasis.jvmwrapper.core.SystemInfo
import ru.itbasis.jvmwrapper.core.checksum256
import ru.itbasis.kotlin.utils.copyTo
import java.io.File

abstract class AbstractDownloader {
	protected abstract val logger: KLogger

	abstract val remoteArchiveFile: RemoteArchiveFile

	open fun auth() {}

	open fun license() {}

	fun download(target: File, downloadProcessListener: DownloadProcessListener? = null) {
		download(remoteArchiveFile = remoteArchiveFile, target = target, downloadProcessListener = downloadProcessListener)
	}

	open fun download(remoteArchiveFile: RemoteArchiveFile, target: File, downloadProcessListener: DownloadProcessListener? = null) {
		if (target.isFile && target.checksum256(remoteArchiveFile.checksum)) {
			return
		}
		auth()
		license()
		val entity = httpClient.getEntity(remoteArchiveFile.url)
		val sizeTotal = entity.contentLength

		target.outputStream().use { fos ->
			entity.content.buffered().copyTo(target = fos, listenerStep = sizeTotal / 10_000) { sizeCurrent ->
				return@copyTo downloadProcessListener == null || downloadProcessListener.invoke(
					remoteArchiveFile.url, sizeCurrent, sizeTotal
				)
			}
			fos.flush()
		}
		require(target.isFile) {
			val msg = "$target is not a file"
			logger.error { msg }
			msg
		}
	}

	open fun RemoteArchiveFile.isRequireAuthentication() =
		false

	protected abstract fun String?.getRemoteArchiveFile(): RemoteArchiveFile?

	open fun String.urlWithinHost() =
		this

	val archiveArchitecture = if (SystemInfo.is32Bit) "i586" else "x64"

	val httpClient = HttpClient()
	internal fun String.htmlContent(httpClient: HttpClient): String {
		return httpClient.getContent(urlWithinHost())
	}
}