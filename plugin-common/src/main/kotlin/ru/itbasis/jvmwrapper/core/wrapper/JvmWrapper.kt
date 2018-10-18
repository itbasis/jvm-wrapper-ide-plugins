package ru.itbasis.jvmwrapper.core.wrapper

import org.apache.commons.lang3.SystemUtils
import ru.itbasis.jvmwrapper.core.ProcessStepListener
import ru.itbasis.jvmwrapper.core.downloader.DownloadProcessListener
import ru.itbasis.jvmwrapper.core.downloader.DownloaderFactory
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.jvmwrapper.core.jvm.JvmType.JDK
import ru.itbasis.jvmwrapper.core.jvm.JvmVendor.ORACLE
import ru.itbasis.jvmwrapper.core.jvm.fixFromMac
import ru.itbasis.jvmwrapper.core.step
import ru.itbasis.jvmwrapper.core.unarchiver.UnarchiverFactory
import java.io.File
import java.net.URL

const val SCRIPT_FILE_NAME = "jvmw"
val JVMW_HOME_DIR: File = File(SystemUtils.USER_HOME, ".jvm")

class JvmWrapper(
	private val workingDir: File,
	private val downloadProcessListener: DownloadProcessListener? = null,
	private val stepListener: ProcessStepListener? = null
) {
	init {
		require(workingDir.isDirectory) {
			"'$workingDir' is not a directory"
		}
	}

	private val wrapperProperties: JvmWrapperProperties = JvmWrapperProperties().apply {
		"Reading properties from the working directory".step(stepListener) { append(workingDir.resolve(JVMW_PROPERTY_FILE_NAME)) }
		"Reading properties from the shared directory".step(stepListener) {
			append(
				JVMW_HOME_DIR.resolve(
					JVMW_PROPERTY_FILE_NAME
				)
			)
		}
		"Adding default properties".step(stepListener) { append(DEFAULT_PROPERTIES) }
	}

	private val jvm by lazy {
		Jvm(
			vendor = wrapperProperties.vendor!!, type = wrapperProperties.type!!, version = wrapperProperties.version!!
		)
	}

	private val jvmName: String by lazy { jvm.toString().toLowerCase() }

	val sdkName: String by lazy { "$SCRIPT_FILE_NAME-$jvmName" }

	private val lastUpdateFile by lazy { LastUpdateFile(jvm = jvm) }

	val jvmHomeDir: File = JVMW_HOME_DIR.resolve(jvmName).run {
		checkAndDownloadJvm(this)
		return@run this
	}.run {
		this.toPath().fixFromMac().toFile()
	}.apply {
		check(isDirectory) {
			"jvm home directory is not exists: $this"
		}
	}

	private fun checkAndDownloadJvm(jvmHomeDir: File) {
		val provider = DownloaderFactory.getInstance(jvm)
		"check exists jvm home directory: $jvmHomeDir".step(stepListener) {
			if (jvmHomeDir.isDirectory && !lastUpdateFile.isExpired()) {
				return
			}

			val remoteArchiveFile = "download jvm archive...".step(stepListener) {
				"specifying the URL for the JRE archive...".step(stepListener) {
					provider.remoteArchiveFile
				}
			}
			if (wrapperProperties.debug!!) {
				println("remoteArchiveFile=$remoteArchiveFile")
			}
			if (lastUpdateFile.equals(remoteArchiveFile = remoteArchiveFile)) {
				lastUpdateFile.update(remoteArchiveFile = remoteArchiveFile)
				return
			}
			val archiveFile = "download remote archive: ${remoteArchiveFile.url}".step(stepListener) {
				File(JVMW_HOME_DIR, "$jvmName.${jvm.archiveFileExtension}").apply {
					provider.download(target = this, downloadProcessListener = downloadProcessListener)
				}
			}
			if (wrapperProperties.debug!!) {
				println("archiveFile=$archiveFile")
			}
			"unpack JRE archive file".step(stepListener) {
				UnarchiverFactory.getInstance(archiveFile, jvmHomeDir, stepListener).unpack()
			}
			lastUpdateFile.update(remoteArchiveFile)
		}
	}

	companion object {
		val DEFAULT_PROPERTIES: JvmWrapperProperties = JvmWrapperProperties(
			vendor = ORACLE, jvmType = JDK, version = DEFAULT_JVM_VERSION, debug = false, oracleKeychainName = ORACLE_KEYCHAIN_DEFAULT_NAME
		)

		private const val REMOTE_SCRIPT_URL = "https://raw.githubusercontent.com/itbasis/jvm-wrapper/master/jvmw"

		fun upgrade(targetDir: File) {
			File(targetDir, SCRIPT_FILE_NAME).run {
				URL(REMOTE_SCRIPT_URL).openStream().copyTo(outputStream())
				return@run setExecutable(true)
			}
		}
	}
}
