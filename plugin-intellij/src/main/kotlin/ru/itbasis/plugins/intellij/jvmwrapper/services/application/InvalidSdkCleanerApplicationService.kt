package ru.itbasis.plugins.intellij.jvmwrapper.services.application

import com.intellij.openapi.application.Application
import com.intellij.openapi.application.WriteActionAware
import com.intellij.openapi.projectRoots.JavaSdk
import com.intellij.openapi.projectRoots.ProjectJdkTable
import ru.itbasis.jvmwrapper.core.jvm.detectors.JvmVersionDetector

class InvalidSdkCleanerApplicationService(
	private val application: Application, private val projectJdkTable: ProjectJdkTable, private val javaSdk: JavaSdk
) : WriteActionAware, Runnable {
	override fun run() {
		application.runWriteAction {
			projectJdkTable.getSdksOfType(javaSdk).filter { sdk ->
				sdk.homePath.let {
					return@let try {
						it.isNullOrBlank() || JvmVersionDetector.detect(path = it!!).isNullOrEmpty()
					} catch (e: IllegalArgumentException) {
						true
					}
				}
			}.forEach { sdk ->
				// TODO https://github.com/itbasis/jvm-wrapper-ide-plugins/issues/4
				projectJdkTable.removeJdk(sdk)
			}
		}
	}
}
