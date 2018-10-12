package ru.itbasis.plugins.intellij.jvmwrapper.services

import com.intellij.openapi.application.Application
import com.intellij.openapi.application.WriteActionAware
import com.intellij.openapi.projectRoots.JavaSdk
import com.intellij.openapi.projectRoots.JdkUtil
import com.intellij.openapi.projectRoots.ProjectJdkTable
import java.io.File

class InvalidSdkCleanerService(
	private val application: Application, private val projectJdkTable: ProjectJdkTable, private val javaSdk: JavaSdk
) : WriteActionAware, Runnable {
	override fun run() {
		application.runWriteAction {
			projectJdkTable.getSdksOfType(javaSdk).filterNot { sdk ->
				JdkUtil.checkForJdk(File(sdk.homePath))
			}.forEach { sdk ->
				// TODO https://github.com/itbasis/jvm-wrapper-ide-plugins/issues/4
				projectJdkTable.removeJdk(sdk)
			}
		}
	}
}
