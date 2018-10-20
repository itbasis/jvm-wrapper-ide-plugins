package ru.itbasis.plugins.intellij.jvmwrapper.services.application

import com.intellij.openapi.application.Application
import com.intellij.openapi.application.WriteActionAware
import com.intellij.openapi.projectRoots.JavaSdk
import com.intellij.openapi.projectRoots.JdkUtil
import com.intellij.openapi.projectRoots.ProjectJdkTable

class InvalidSdkCleanerApplicationService(
	private val application: Application, private val projectJdkTable: ProjectJdkTable, private val javaSdk: JavaSdk
) : WriteActionAware, Runnable {
	override fun run() {
		application.runWriteAction {
			projectJdkTable.getSdksOfType(javaSdk).filterNot { sdk ->
				!sdk.homePath.isNullOrEmpty() && JdkUtil.checkForJdk(sdk.homePath!!)
			}.forEach { sdk ->
				// TODO https://github.com/itbasis/jvm-wrapper-ide-plugins/issues/4
				projectJdkTable.removeJdk(sdk)
			}
		}
	}
}
