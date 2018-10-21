package ru.itbasis.plugins.intellij.jvmwrapper.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.ServiceManager
import ru.itbasis.plugins.intellij.jvmwrapper.services.application.InvalidSdkCleanerApplicationService
import ru.itbasis.plugins.intellij.jvmwrapper.services.application.ProjectSdkScannerApplicationService

class InvalidSdkCleanerAction : AnAction("Clean invalid Sdk && refresh") {
	override fun actionPerformed(event: AnActionEvent) {
		ServiceManager.getService(InvalidSdkCleanerApplicationService::class.java).run()
		ServiceManager.getService(ProjectSdkScannerApplicationService::class.java).run()
	}
}
