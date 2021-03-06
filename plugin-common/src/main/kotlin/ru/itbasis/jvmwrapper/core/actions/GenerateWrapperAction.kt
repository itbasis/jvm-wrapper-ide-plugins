package ru.itbasis.jvmwrapper.core.actions

import ru.itbasis.jvmwrapper.core.SystemInfo.NEW_LINE
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.jvmwrapper.core.wrapper.JVMW_PROPERTY_FILE_NAME
import ru.itbasis.jvmwrapper.core.wrapper.JvmWrapper
import ru.itbasis.jvmwrapper.core.wrapper.JvmWrapper.Companion.DEFAULT_PROPERTIES
import ru.itbasis.jvmwrapper.core.wrapper.JvmWrapperPropertyKeys
import java.io.File
import java.util.Properties

class GenerateWrapperAction(
	private val parentDir: File
) : Runnable {

	override fun run() {
		JvmWrapper.upgrade(parentDir)

		File(parentDir, JVMW_PROPERTY_FILE_NAME).apply {
			Properties().toString()
			writeText(
				arrayOf(
					"${JvmWrapperPropertyKeys.JVM_VERSION}=" + Jvm(
						vendor = DEFAULT_PROPERTIES.vendor!!, type = DEFAULT_PROPERTIES.type!!, version = DEFAULT_PROPERTIES.version!!
					).cleanVersion,
					"${JvmWrapperPropertyKeys.JVM_VENDOR}=" + DEFAULT_PROPERTIES.vendor!!.code,
					"${JvmWrapperPropertyKeys.JVMW_DEBUG}=" + if (DEFAULT_PROPERTIES.debug!!) "Y" else "N",
					"${JvmWrapperPropertyKeys.JVM_TYPE}=" + DEFAULT_PROPERTIES.type!!.name
				).joinToString(NEW_LINE)
			)
		}
	}
}
