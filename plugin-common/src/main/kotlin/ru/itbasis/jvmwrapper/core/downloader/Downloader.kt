package ru.itbasis.jvmwrapper.core.downloader

import org.apache.commons.lang3.SystemUtils
import ru.itbasis.jvmwrapper.core.HttpClient
import ru.itbasis.jvmwrapper.core.SystemInfo
import ru.itbasis.jvmwrapper.core.downloader.oracle.OracleDownloader
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.jvmwrapper.core.jvm.JvmVendor
import java.io.File

abstract class Downloader {
  companion object {
    @JvmStatic
    fun getInstance(jvm: Jvm): Downloader {
      return when (jvm.vendor) {
        JvmVendor.ORACLE -> OracleDownloader(jvm)

        else -> throw IllegalArgumentException("Unsupported jvm vendor: ${jvm.vendor}")
      }
    }
  }

  abstract val remoteArchiveFile: RemoteArchiveFile

  abstract fun download(target: File, downloadProcessListener: DownloadProcessListener? = null)

  open fun auth() {}
  open fun license() {}

  open fun RemoteArchiveFile.isRequireAuthentication() = false

  abstract fun String?.getRemoteArchiveFile(): RemoteArchiveFile?

  open fun String.urlWithinHost() = this

  val archiveArchitecture = if (SystemInfo.is32Bit) "i586" else "x64"

  val archiveExtension = when {
    SystemUtils.IS_OS_MAC -> "dmg"
    SystemUtils.IS_OS_WINDOWS -> "exe"
    else -> "tar.gz"
  }

  val httpClient = HttpClient()
  internal fun String.htmlContent(httpClient: HttpClient): String {
    return httpClient.getContent(urlWithinHost())
  }
}