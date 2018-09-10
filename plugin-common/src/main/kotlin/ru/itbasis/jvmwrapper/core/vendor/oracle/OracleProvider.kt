package ru.itbasis.jvmwrapper.core.vendor.oracle

import com.google.gson.JsonParser
import mu.KotlinLogging
import org.apache.commons.codec.digest.DigestUtils
import org.apache.http.cookie.ClientCookie
import org.apache.http.impl.cookie.BasicClientCookie2
import ru.itbasis.jvmwrapper.core.RemoteArchiveFile
import ru.itbasis.jvmwrapper.core.findOne
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.jvmwrapper.core.vendor.AbstractProvider
import ru.itbasis.jvmwrapper.core.vendor.DownloadProcessListener
import ru.itbasis.kotlin.utils.copyTo
import java.io.File
import kotlin.text.RegexOption.IGNORE_CASE

class OracleProvider(private val jvm: Jvm) : AbstractProvider() {
  private val logger = KotlinLogging.logger {}

  override val remoteArchiveFile: RemoteArchiveFile by lazy {
    latestDownloadPageUrl.getRemoteArchiveFile() ?: archiveDownloadPageUrl.getRemoteArchiveFile()
    ?: throw IllegalStateException("it was not succeeded to define URL for version $jvm")
  }

  override fun String.urlWithinHost() = (if (startsWith("/")) "http://www.oracle.com$this" else this)

  override fun String?.getRemoteArchiveFile(): RemoteArchiveFile? {
    require(!this.isNullOrBlank()) {
      val msg = "request url null or empty"
      logger.error { msg }
      msg
    }
    val result = regexDownloadFile.findOne(this!!.htmlContent(httpClient = httpClient)) ?: return null

    return JsonParser().parse(result).asJsonObject.let { jsonObject ->
      RemoteArchiveFile(
        url = jsonObject.get("filepath").asString, checksum = jsonObject.get("SHA256").asString
      )
    }
  }

  override fun download(
    target: File, downloadProcessListener: DownloadProcessListener?
  ) {
    if (target.isFile && remoteArchiveFile.checksum.isNotEmpty() && remoteArchiveFile.checksum == DigestUtils.sha256Hex(
        target.inputStream()
      )
    ) {
      return
    }
    auth()
    license()
    val entity = httpClient.getEntity(remoteArchiveFile.url)
    val sizeTotal = entity.contentLength

    target.outputStream().use { fos ->
      entity.content.buffered().copyTo(target = fos, listenerStep = sizeTotal / 10_000) { sizeCurrent ->
        return@copyTo downloadProcessListener?.invoke(
          remoteArchiveFile.url, sizeCurrent, sizeTotal
        ) == true
      }
      fos.flush()
    }
    require(target.isFile) {
      val msg = "$target is not a file"
      logger.error { msg }
      msg
    }
  }

  override fun auth() {
    if (!remoteArchiveFile.isRequireAuthentication()) return

    OracleAuthenticator(httpClient = httpClient).execute()
  }

  override fun license() {
    val cookieOracleLicense = BasicClientCookie2("oraclelicense", "accept-securebackup-cookie")
    cookieOracleLicense.setAttribute(ClientCookie.DOMAIN_ATTR, true.toString())
    cookieOracleLicense.domain = ".oracle.com"
    httpClient.httpCookieStore.addCookie(cookieOracleLicense)
  }

  private val regexPreviousReleasesPage: Regex by lazy {
    """<a title="Previous Releases" href="(.*)">""".toRegex(IGNORE_CASE)
  }

  private val regexDownloadFile: Regex by lazy {
    """downloads\['${jvm.type}-${jvm.cleanVersion}-oth-JPR']\['files']\['.*${jvm.os}.*$archiveArchitecture.*\.$archiveExtension'] = (.*);""".toRegex(
      IGNORE_CASE
    )
  }

  private val indexPage: String by lazy { httpClient.getContent("/technetwork/java/javase/downloads/index.html".urlWithinHost()) }

  private val latestDownloadPageUrl: String? by lazy {
    """<a name="${jvm.type}${jvm.major}" href="(.*)"""".toRegex(
      IGNORE_CASE
    ).findOne(indexPage)
  }

  private val archiveDownloadPageUrl: String? by lazy {
    val previousReleasesPage = regexPreviousReleasesPage.find(indexPage)?.groupValues?.get(1)?.let {
      return@let httpClient.getContent(it.urlWithinHost())
    } ?: throw IllegalStateException("not found previous releases page")

    val regex = """<a.*href="(.*java-archive-javase${jvm.major}-\d+\.html)".*>\s*Java SE ${jvm.major}\s*</a>""".toRegex(
      IGNORE_CASE
    )

    val matchResult = regex.find(previousReleasesPage)
    return@lazy matchResult?.groupValues?.get(1) ?: throw IllegalStateException("not found download archive page")
  }

  override fun RemoteArchiveFile.isRequireAuthentication() = !url.contains("otn-pub")
}
