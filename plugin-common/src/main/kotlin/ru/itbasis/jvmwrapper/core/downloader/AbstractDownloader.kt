package ru.itbasis.jvmwrapper.core.downloader

import mu.KLogger
import org.apache.commons.lang3.SystemUtils
import org.apache.commons.lang3.SystemUtils.IS_OS_MAC
import ru.itbasis.jvmwrapper.core.FileArchitecture.I586
import ru.itbasis.jvmwrapper.core.FileArchitecture.X64
import ru.itbasis.jvmwrapper.core.FileArchitecture.X86_64
import ru.itbasis.jvmwrapper.core.FileNameExtension
import ru.itbasis.jvmwrapper.core.HttpClient
import ru.itbasis.jvmwrapper.core.OsType.LINUX
import ru.itbasis.jvmwrapper.core.OsType.OSX
import ru.itbasis.jvmwrapper.core.OsType.WINDOWS
import ru.itbasis.jvmwrapper.core.SystemInfo.is32Bit
import ru.itbasis.jvmwrapper.core.checksum256
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.kotlin.utils.copyTo
import java.io.File

abstract class AbstractDownloader(protected val jvm: Jvm) {
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
			return@require msg
		}
	}

	open fun RemoteArchiveFile.isRequireAuthentication() =
		false

	protected abstract fun String?.getRemoteArchiveFile(): RemoteArchiveFile?

	open fun String.urlWithinHost() =
		this

	open val archiveArchitecture = when {
		is32Bit   -> I586
		IS_OS_MAC -> X86_64
		else      -> X64
	}

	internal open val archiveFileExtension = when {
		jvm.major >= 11 && SystemUtils.IS_OS_WINDOWS -> FileNameExtension.ZIP

		SystemUtils.IS_OS_MAC                        -> FileNameExtension.DMG
		SystemUtils.IS_OS_WINDOWS                    -> FileNameExtension.EXE

		else                                         -> FileNameExtension.TAR_GZ
	}

	internal open val os = when {
		IS_OS_MAC                 -> if (jvm.major > 8) OSX.toString() else "macosx"
		SystemUtils.IS_OS_WINDOWS -> WINDOWS.toString()
		else                      -> LINUX.toString()
	}

	val httpClient = HttpClient()
	internal fun String.htmlContent(httpClient: HttpClient): String {
		return httpClient.getContent(urlWithinHost())
	}
}