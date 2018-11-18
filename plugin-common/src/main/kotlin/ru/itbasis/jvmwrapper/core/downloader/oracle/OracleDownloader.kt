package ru.itbasis.jvmwrapper.core.downloader.oracle

import com.google.gson.JsonParser
import mu.KotlinLogging
import org.apache.http.cookie.ClientCookie
import org.apache.http.impl.cookie.BasicClientCookie2
import ru.itbasis.jvmwrapper.core.downloader.AbstractDownloader
import ru.itbasis.jvmwrapper.core.downloader.RemoteArchiveFile
import ru.itbasis.jvmwrapper.core.findOne
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import kotlin.text.RegexOption.IGNORE_CASE

class OracleDownloader(jvm: Jvm) : AbstractDownloader(jvm = jvm) {
	override val logger = KotlinLogging.logger {}

	override val remoteArchiveFile: RemoteArchiveFile by lazy {
		latestDownloadPageUrl.getRemoteArchiveFile()
		?: archiveDownloadPageUrl.getRemoteArchiveFile()
		?: throw IllegalStateException("it was not succeeded to define URL for version $jvm")
	}

	override fun String.urlWithinHost() =
		(if (startsWith("/")) "https://www.oracle.com$this" else this)

	override fun String?.getRemoteArchiveFile(): RemoteArchiveFile? {
		logger.debug { "this: $this" }
		if (this.isNullOrEmpty()) {
			return null
		}

		val htmlContent = this!!.htmlContent(httpClient = httpClient)
		val result = regexDownloadFile.findOne(htmlContent)
		             ?: return null

		return JsonParser().parse(result).asJsonObject.let { jsonObject ->
			RemoteArchiveFile(
				url = jsonObject.get("filepath").asString, checksum = jsonObject.get("SHA256").asString, archiveFileExtension = archiveFileExtension
			)
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
		val regexp =
			"""downloads\['${jvm.type}-${jvm.cleanVersion}-oth-JPR']\['files']\['.*${jvm.osAsString}.*$archiveArchitecture.*\.$archiveFileExtension'] = (.*);"""
		logger.debug { "regexp: $regexp" }
		regexp.toRegex(
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
		}
		                           ?: throw IllegalStateException("not found previous releases page")

		val regex = """<a.*href="(.*java-archive-javase${jvm.major}-\d+\.html)".*>\s*Java SE ${jvm.major}\s*</a>""".toRegex(
			IGNORE_CASE
		)

		val matchResult = regex.find(previousReleasesPage)
		return@lazy matchResult?.groupValues?.get(1)
		            ?: throw IllegalStateException("not found download archive page")
	}

	override fun RemoteArchiveFile.isRequireAuthentication() =
		!url.contains("otn-pub")
}
