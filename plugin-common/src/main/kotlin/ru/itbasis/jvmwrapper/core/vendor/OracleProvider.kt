package ru.itbasis.jvmwrapper.core.vendor

import com.google.gson.JsonParser
import org.apache.commons.codec.digest.DigestUtils
import org.apache.http.client.methods.HttpGet
import org.apache.http.cookie.ClientCookie
import org.apache.http.impl.cookie.BasicClientCookie2
import ru.itbasis.jvmwrapper.core.JvmVersion
import ru.itbasis.jvmwrapper.core.RemoteArchiveFile
import ru.itbasis.kotlin.utils.copyTo
import java.io.File
import kotlin.text.RegexOption.IGNORE_CASE

class OracleProvider(private val jvmVersion: JvmVersion) : AbstractProvider() {
	override val remoteArchiveFile: RemoteArchiveFile
		get() {
			return latestDownloadPageUrl.getRemoteArchiveFile()
				?: archiveDownloadPageUrl.getRemoteArchiveFile()
				?: throw IllegalStateException("it was not succeeded to define URL for version $jvmVersion")
		}

	override fun String.urlWithinHost() =
		(if (startsWith("/")) "http://www.oracle.com$this" else this)

	override fun String?.getRemoteArchiveFile(): RemoteArchiveFile? {
//    logger.debug { "url: $this" }
		require(!this.isNullOrBlank()) { "request url null or empty" }
		val result = regexDownloadFile.findOne(this!!.htmlContent()) ?: return null

		return JsonParser().parse(result).asJsonObject.let { jsonObject ->
			RemoteArchiveFile(
				url = jsonObject.get("filepath").asString, checksum = jsonObject.get("SHA256").asString
			)
		}
	}

	override fun download(
		remoteArchiveFile: RemoteArchiveFile,
		target: File,
		downloadProcessListener: DownloadProcessListener?
	) {
		if (target.isFile && remoteArchiveFile.checksum.isNotEmpty() && remoteArchiveFile.checksum == DigestUtils.sha256Hex(
				target.inputStream()
			)
		) {
			return
		}
		// TODO
		if (remoteArchiveFile.isRequireAuthentication()) println("auth!")

		val cookieOracleLicense = BasicClientCookie2("oraclelicense", "accept-securebackup-cookie")
		cookieOracleLicense.setAttribute(ClientCookie.DOMAIN_ATTR, true.toString())
		cookieOracleLicense.domain = ".oracle.com"


		httpCookieStore.addCookie(cookieOracleLicense)
		val httpGet = HttpGet(remoteArchiveFile.url)
		val httpResponse = httpClient.execute(httpGet, httpContext)
		val entity = httpResponse.entity
		val sizeTotal = entity.contentLength

		target.outputStream().use { fos ->
			entity.content.buffered()
				.copyTo(target = fos, listenerStep = sizeTotal / 10_000) { sizeCurrent ->
					return@copyTo downloadProcessListener?.invoke(
						remoteArchiveFile.url,
						sizeCurrent,
						sizeTotal
					) == true
				}
			fos.flush()
		}
	}

	private val regexPreviousReleasesPage: Regex
		get() = """<a title="Previous Releases" href="(.*)">""".toRegex(IGNORE_CASE)

	private val regexDownloadFile: Regex
		get() {
			return """downloads\['${jvmVersion.type}-${jvmVersion.cleanVersion}-oth-JPR']\['files']\['.*${jvmVersion.os}.*$archiveArchitecture.*\.$archiveExtension'] = (.*);""".toRegex(
				IGNORE_CASE
			)
		}

	private val indexPage: String
		get() = "/technetwork/java/javase/downloads/index.html".htmlContent()

	private val latestDownloadPageUrl: String?
		get() = """<a name="${jvmVersion.type}${jvmVersion.major}" href="(.*)"""".toRegex(
			IGNORE_CASE
		).findOne(indexPage)

	private val archiveDownloadPageUrl: String?
		get() {
			val previousReleasesPage =
				regexPreviousReleasesPage.find(indexPage)?.groupValues?.get(1)?.htmlContent()
					?: throw IllegalStateException("not found previous releases page")
			val regex =
				"""<a target="" href="(.*java-archive-javase${jvmVersion.major}-\d+\.html)">Java SE ${jvmVersion.major}</a>""".toRegex(
					IGNORE_CASE
				)
			val matchResult = regex.find(previousReleasesPage)
			return matchResult?.groupValues?.get(1)
				?: throw IllegalStateException("not found download archive page")
		}

	override fun RemoteArchiveFile.isRequireAuthentication() = !url.contains("otn-pub")
}
