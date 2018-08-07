package ru.itbasis.jvmwrapper.core.actions

import org.apache.commons.lang3.StringUtils
import ru.itbasis.jvmwrapper.core.JvmVersion
import ru.itbasis.jvmwrapper.core.JvmWrapper
import ru.itbasis.jvmwrapper.core.JvmWrapper.Companion.DEFAULT_PROPERTIES
import ru.itbasis.jvmwrapper.core.JvmWrapper.Companion.REMOTE_SCRIPT_URL
import ru.itbasis.jvmwrapper.core.JvmWrapper.Companion.SCRIPT_FILE_NAME
import ru.itbasis.jvmwrapper.core.JvmWrapperPropertyKeys
import java.io.File
import java.net.URL

class GenerateWrapperAction(
  private val parentDir: File
) : Runnable {

  override fun run() {
    URL(REMOTE_SCRIPT_URL).openStream().copyTo(File(parentDir, SCRIPT_FILE_NAME).outputStream())


    File(parentDir, JvmWrapper.JVMW_PROPERTY_FILE_NAME).apply {
      writeText("${JvmWrapperPropertyKeys.JVM_VERSION}=" + JvmVersion(version = DEFAULT_PROPERTIES.version!!).cleanVersion)
      arrayOf(
        "${JvmWrapperPropertyKeys.JVM_VENDOR}=" + DEFAULT_PROPERTIES.vendor!!.code,
        "${JvmWrapperPropertyKeys.JVMW_DEBUG}=" + if (DEFAULT_PROPERTIES.debug!!) "Y" else "N",
        "${JvmWrapperPropertyKeys.JVM_TYPE}=" + DEFAULT_PROPERTIES.type!!.name
      ).forEach { value ->
        appendText(StringUtils.LF)
        appendText(value)
      }
    }
  }
}
