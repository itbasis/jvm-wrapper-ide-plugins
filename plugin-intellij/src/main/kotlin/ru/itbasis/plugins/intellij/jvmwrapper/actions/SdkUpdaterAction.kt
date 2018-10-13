package ru.itbasis.plugins.intellij.jvmwrapper.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.ServiceManager
import ru.itbasis.jvmwrapper.core.wrapper.JvmWrapper
import ru.itbasis.plugins.intellij.jvmwrapper.services.application.ProjectSdkScannerApplicationService
import ru.itbasis.plugins.intellij.jvmwrapper.services.project.JvmWrapperProjectService
import ru.itbasis.plugins.intellij.jvmwrapper.services.project.SdkUpdaterProjectService
import java.io.File

class SdkUpdaterAction : AnAction("Refresh") {
	override fun update(event: AnActionEvent) {
		event.presentation.isEnabledAndVisible = ServiceManager.getService(event.project!!, JvmWrapperProjectService::class.java).hasWrapper()
	}

	override fun actionPerformed(event: AnActionEvent) {
		val project = event.project!!

		ServiceManager.getService(ProjectSdkScannerApplicationService::class.java).run()
		ServiceManager.getService(project, SdkUpdaterProjectService::class.java).run()

		JvmWrapper.upgrade(File(project.basePath))
	}
}
