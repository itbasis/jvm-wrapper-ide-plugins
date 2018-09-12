package ru.itbasis.jvmwrapper.core.wrapper

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.SystemUtils
import org.apache.commons.lang3.SystemUtils.IS_OS_MAC
import ru.itbasis.jvmwrapper.core.ProcessStepListener
import ru.itbasis.jvmwrapper.core.downloader.RemoteArchiveFile
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.jvmwrapper.core.jvm.JvmType.JDK
import ru.itbasis.jvmwrapper.core.step
import ru.itbasis.jvmwrapper.core.unarchiver.UnarchiverFactory
import ru.itbasis.jvmwrapper.core.vendor.DownloadProcessListener
import ru.itbasis.jvmwrapper.core.vendor.JvmVendor.ORACLE
import ru.itbasis.jvmwrapper.core.vendor.oracle.OracleProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar

const val SCRIPT_FILE_NAME = "jvmw"

class JvmWrapper(
  private val workingDir: File,
  private val downloadProcessListener: DownloadProcessListener? = null,
  private val stepListener: ProcessStepListener? = null
) {
  init {
    require(workingDir.isDirectory) { "'$workingDir' is not a directory" }
  }

  private val wrapperProperties: JvmWrapperProperties = JvmWrapperProperties().apply {
    "Reading properties from the working directory".step(stepListener) { workingDir.resolve(JVMW_PROPERTY_FILE_NAME).let { append(it) } }
    "Reading properties from the shared directory".step(stepListener) { JVMW_HOME_DIR.resolve(JVMW_PROPERTY_FILE_NAME).let { append(it) } }
    "Adding default properties".step(stepListener) { append(DEFAULT_PROPERTIES) }
  }

  private val jvmVersion = Jvm(vendor = wrapperProperties.vendor!!, type = wrapperProperties.type!!, version = wrapperProperties.version!!)

  private val jvmName: String = "${wrapperProperties.vendor}-${jvmVersion.type}-${jvmVersion.cleanVersion}".toLowerCase()

  val sdkName: String by lazy { "$SCRIPT_FILE_NAME-$jvmName" }

  val jvmHomeDir: File = JVMW_HOME_DIR.resolve(jvmName).also { jvmHomeDir ->
    val provider = OracleProvider(jvm = jvmVersion)

    "check exists jvm home directory: $jvmHomeDir".step(stepListener) {
      if (jvmHomeDir.isDirectory) {
        updateLastUpdateFile(provider.remoteArchiveFile)
        return@step
      }
      "download jvm archive...".step(stepListener) {
        val remoteFile = "specifying the URL for the JRE archive...".step(stepListener) { provider.remoteArchiveFile }

        "download remote archive: ${remoteFile.url}".step(stepListener) {
          File(JVMW_HOME_DIR, "$jvmName.${provider.archiveExtension}").apply {
            provider.download(target = this, downloadProcessListener = downloadProcessListener)
          }
        }
      }.let { archiveFile ->
        "unpack JRE archive file".step(stepListener) {
          UnarchiverFactory.getInstance(archiveFile, jvmHomeDir, stepListener).unpack()
        }
        updateLastUpdateFile(provider.remoteArchiveFile)
      }
    }
  }.run {
    if (IS_OS_MAC) this.resolve("Home") else this
  }.apply {
    check(isDirectory) { "jvm home directory is not exists: $this" }
  }

  private fun updateLastUpdateFile(remoteArchiveFile: RemoteArchiveFile) {
    File(JVMW_HOME_DIR, "$jvmName.last_update").run {
      writeText(SDF.format(Calendar.getInstance().time))
      appendText(StringUtils.LF)
      appendText(remoteArchiveFile.url)
      appendText(StringUtils.LF)
      appendText(remoteArchiveFile.checksum)
      appendText(StringUtils.LF)
    }
  }

  companion object {
    val DEFAULT_PROPERTIES: JvmWrapperProperties = JvmWrapperProperties(
      vendor = ORACLE, jvmType = JDK, version = DEFAULT_JVM_VERSION, debug = false, oracleKeychainName = ORACLE_KEYCHAIN_DEFAULT_NAME
    )
    const val REMOTE_SCRIPT_URL = "https://raw.githubusercontent.com/itbasis/jvm-wrapper/master/jvmw"

    val JVMW_HOME_DIR: File = File(SystemUtils.USER_HOME, ".jvm")

    @JvmStatic
    val SDF = SimpleDateFormat("yyyy-MM-dd HH:mm")
  }
}
