package ru.itbasis.jvmwrapper.core.actions

import org.apache.commons.lang3.StringUtils
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.jvmwrapper.core.wrapper.JVMW_PROPERTY_FILE_NAME
import ru.itbasis.jvmwrapper.core.wrapper.JvmWrapper.Companion.DEFAULT_PROPERTIES
import ru.itbasis.jvmwrapper.core.wrapper.JvmWrapper.Companion.REMOTE_SCRIPT_URL
import ru.itbasis.jvmwrapper.core.wrapper.JvmWrapperPropertyKeys
import ru.itbasis.jvmwrapper.core.wrapper.SCRIPT_FILE_NAME
import java.io.File
import java.net.URL

class GenerateWrapperAction(
  private val parentDir: File
) : Runnable {

  override fun run() {
    URL(REMOTE_SCRIPT_URL).openStream().copyTo(File(parentDir, SCRIPT_FILE_NAME).outputStream())


    File(parentDir, JVMW_PROPERTY_FILE_NAME).apply {
      writeText(
        "${JvmWrapperPropertyKeys.JVM_VERSION}=" + Jvm(
          vendor = DEFAULT_PROPERTIES.vendor!!, type = DEFAULT_PROPERTIES.type!!, version = DEFAULT_PROPERTIES.version!!
        ).cleanVersion
      )
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
