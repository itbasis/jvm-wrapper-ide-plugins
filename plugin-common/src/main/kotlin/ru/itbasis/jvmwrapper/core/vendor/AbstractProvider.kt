package ru.itbasis.jvmwrapper.core.vendor

import org.apache.commons.lang3.SystemUtils.IS_OS_MAC
import org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS
import org.apache.http.HttpResponse
import org.apache.http.client.CookieStore
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.protocol.HttpClientContext.COOKIE_STORE
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.client.LaxRedirectStrategy
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.protocol.BasicHttpContext
import org.apache.http.protocol.HttpContext
import org.apache.http.util.EntityUtils
import ru.itbasis.jvmwrapper.core.RemoteArchiveFile
import ru.itbasis.jvmwrapper.core.SystemInfo.is32Bit
import java.io.File

typealias DownloadProcessListener = (remoteArchiveUrl: String, sizeCurrent: Long, sizeTotal: Long) -> Boolean

abstract class AbstractProvider {
  abstract val remoteArchiveFile: RemoteArchiveFile?

  fun download(target: File, downloadProcessListener: DownloadProcessListener? = null) =
    download(remoteArchiveFile!!, target, downloadProcessListener)

  abstract fun download(
    remoteArchiveFile: RemoteArchiveFile, target: File, downloadProcessListener: DownloadProcessListener? = null
  )

  open fun RemoteArchiveFile.isRequireAuthentication() = false

  abstract fun String?.getRemoteArchiveFile(): RemoteArchiveFile?

  open fun String.urlWithinHost() = this

  val archiveArchitecture = if (is32Bit) "i586" else "x64"

  val archiveExtension = when {
    IS_OS_MAC -> "dmg"
    IS_OS_WINDOWS -> "exe"
    else -> "tar.gz"
  }

  protected val httpCookieStore: CookieStore = BasicCookieStore()
  protected val httpContext: HttpContext = BasicHttpContext().also {
    it.setAttribute(COOKIE_STORE, httpCookieStore)
  }

  protected val httpClient: CloseableHttpClient =
    HttpClients.custom().setUserAgent("Mozilla/5.0 https://github.com/itbasis/jvm-wrapper").setConnectionManager(
      PoolingHttpClientConnectionManager()
    ).setRedirectStrategy(LaxRedirectStrategy()).build()!!

  protected fun String.htmlContent(): String {
    return httpClient.execute(HttpGet(urlWithinHost()), httpContext).content()
  }

  private fun HttpResponse.content(): String {
    return EntityUtils.toString(entity)
  }

  protected fun Regex.findOne(content: String): String? {
    return find(content)?.groupValues?.get(1)
  }
}
