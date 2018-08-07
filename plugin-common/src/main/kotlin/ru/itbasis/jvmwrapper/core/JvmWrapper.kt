package ru.itbasis.jvmwrapper.core

import org.apache.commons.lang3.SystemUtils
import org.apache.commons.lang3.SystemUtils.IS_OS_MAC
import ru.itbasis.jvmwrapper.core.unarchiver.UnarchiverFactory
import ru.itbasis.jvmwrapper.core.vendor.DownloadProcessListener
import ru.itbasis.jvmwrapper.core.vendor.JvmVendor
import ru.itbasis.jvmwrapper.core.vendor.OracleProvider
import java.io.File

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

  private val jvmVersion = JvmVersion(type = wrapperProperties.type!!, version = wrapperProperties.version!!)

  val jvmName: String = "${wrapperProperties.vendor}-${jvmVersion.type}-${jvmVersion.cleanVersion}".toLowerCase()

  val jvmHomeDir: File = JVMW_HOME_DIR.resolve(jvmName).also { jvmHomeDir ->
    "check exists jvm home directory: $jvmHomeDir".step(stepListener) {
      if (jvmHomeDir.isDirectory) return@step
      "download jvm archive...".step(stepListener) {
        val provider = OracleProvider(jvmVersion = jvmVersion)
        val remoteFile = "specifying the URL for the JVM archive...".step(stepListener) { provider.remoteArchiveFile }

        "download remote archive: ${remoteFile.url}".step(stepListener) {
          File(JVMW_HOME_DIR, "$jvmName.${provider.archiveExtension}").apply {
            provider.download(remoteArchiveFile = remoteFile, target = this, downloadProcessListener = downloadProcessListener)
          }
        }
      }.let { archiveFile ->
        "unpack JVM archive file".step(stepListener) {
          UnarchiverFactory.getInstance(archiveFile, jvmHomeDir, stepListener).unpack()
        }
      }
    }
  }.run {
    if (IS_OS_MAC) this.resolve("Home") else this
  }.apply {
    check(isDirectory) { "jvm home directory is not exists: $this" }
  }

  companion object {
    const val SCRIPT_FILE_NAME = "jvmw"
    const val JVMW_PROPERTY_FILE_NAME = "$SCRIPT_FILE_NAME.properties"
    val DEFAULT_PROPERTIES: JvmWrapperProperties = JvmWrapperProperties(
      vendor = JvmVendor.ORACLE,
      jvmType = JvmType.JDK,
      version = DEFAULT_JVM_VERSION,
      debug = false,
      oracleKeychainName = ORACLE_KEYCHAIN_DEFAULT_NAME
    )
    const val REMOTE_SCRIPT_URL = "https://raw.githubusercontent.com/itbasis/jvm-wrapper/master/jvmw"

    val JVMW_HOME_DIR: File = File(SystemUtils.USER_HOME, ".jvm")
  }
}
