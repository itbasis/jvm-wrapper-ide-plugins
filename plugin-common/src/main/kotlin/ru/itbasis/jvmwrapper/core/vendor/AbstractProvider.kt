package ru.itbasis.jvmwrapper.core.vendor

import org.apache.commons.lang3.SystemUtils.IS_OS_MAC
import org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS
import ru.itbasis.jvmwrapper.core.HttpClient
import ru.itbasis.jvmwrapper.core.downloader.RemoteArchiveFile
import ru.itbasis.jvmwrapper.core.SystemInfo.is32Bit
import java.io.File

typealias DownloadProcessListener = (remoteArchiveUrl: String, sizeCurrent: Long, sizeTotal: Long) -> Boolean

abstract class AbstractProvider {
  abstract val remoteArchiveFile: RemoteArchiveFile?

  abstract fun download(target: File, downloadProcessListener: DownloadProcessListener? = null)

  open fun auth() {}
  open fun license() {}

  open fun RemoteArchiveFile.isRequireAuthentication() = false

  abstract fun String?.getRemoteArchiveFile(): RemoteArchiveFile?

  open fun String.urlWithinHost() = this

  val archiveArchitecture = if (is32Bit) "i586" else "x64"

  val archiveExtension = when {
    IS_OS_MAC -> "dmg"
    IS_OS_WINDOWS -> "exe"
    else -> "tar.gz"
  }

  val httpClient = HttpClient()
  internal fun String.htmlContent(httpClient: HttpClient): String {
    return httpClient.getContent(urlWithinHost())
  }
}
