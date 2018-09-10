package ru.itbasis.jvmwrapper.core.wrapper

import ru.itbasis.jvmwrapper.core.jvm.JvmType
import ru.itbasis.jvmwrapper.core.jvm.toJvmType
import ru.itbasis.jvmwrapper.core.vendor.JvmVendor
import ru.itbasis.jvmwrapper.core.vendor.toJvmVendor
import ru.itbasis.kotlin.utils.toBoolean
import java.io.File
import java.util.Properties

enum class JvmWrapperPropertyKeys {
  JVM_VENDOR, JVM_TYPE, JVM_VERSION, JVMW_DEBUG, JVM_REQUIRED_UPDATE, JVMW_ORACLE_KEYCHAIN, ORACLE_USER, ORACLE_PASSWORD, JVMW_USE_SYSTEM_JVM
}

const val DEFAULT_JVM_VERSION = "8u171"
const val JVMW_PROPERTY_FILE_NAME = "$SCRIPT_FILE_NAME.properties"
const val ORACLE_KEYCHAIN_DEFAULT_NAME = "JVM_WRAPPER_ORACLE"

class JvmWrapperProperties(
  vendor: JvmVendor? = null,
  jvmType: JvmType? = null,
  version: String? = null,
  requiredUpdate: Boolean? = null,
  debug: Boolean? = null,
  useSystemJdk: Boolean? = null,
  oracleKeychainName: String? = null
) {
  var vendor: JvmVendor? = vendor
    private set
  var type: JvmType? = jvmType
    private set
  var version: String? = version
    private set
  var requiredUpdate: Boolean? = requiredUpdate
    private set
  var debug: Boolean? = debug
    private set
  var useSystemJdk: Boolean? = useSystemJdk
    private set
  var oracleKeychainName: String? = oracleKeychainName
    private set

  @Suppress("ComplexMethod")
  fun append(propertyFile: File? = null) {
    if (propertyFile == null || !propertyFile.isFile) {
      return
    }
    val properties = Properties()
    properties.load(propertyFile.inputStream())
    properties.forEach { (key, value) ->
      if (key !is String || value !is String) return

      when (key.toUpperCase()) {
        JvmWrapperPropertyKeys.JVM_VENDOR.name -> if (vendor == null) vendor = value.toJvmVendor()
        JvmWrapperPropertyKeys.JVM_TYPE.name -> if (type == null) type = value.toJvmType()
        JvmWrapperPropertyKeys.JVM_VERSION.name -> if (version == null) version = value
        JvmWrapperPropertyKeys.JVM_REQUIRED_UPDATE.name -> if (requiredUpdate == null) requiredUpdate = value.toBoolean()
        JvmWrapperPropertyKeys.JVMW_DEBUG.name -> if (debug == null) debug = value.toBoolean()
        JvmWrapperPropertyKeys.JVMW_USE_SYSTEM_JVM.name -> if (useSystemJdk == null) useSystemJdk = value.toBoolean()
        JvmWrapperPropertyKeys.JVMW_ORACLE_KEYCHAIN.name -> if (oracleKeychainName == null) oracleKeychainName = value
      }
    }
  }

  fun append(jvmWrapperProperties: JvmWrapperProperties) {
    if (vendor == null) vendor = jvmWrapperProperties.vendor
    if (type == null) type = jvmWrapperProperties.type
    if (version.isNullOrEmpty()) version = jvmWrapperProperties.version?.trim()
    if (requiredUpdate == null) requiredUpdate = jvmWrapperProperties.requiredUpdate
    if (debug == null) debug = jvmWrapperProperties.debug
    if (useSystemJdk == null) useSystemJdk = jvmWrapperProperties.useSystemJdk
    if (oracleKeychainName.isNullOrEmpty()) oracleKeychainName = jvmWrapperProperties.oracleKeychainName?.trim()
  }
}
