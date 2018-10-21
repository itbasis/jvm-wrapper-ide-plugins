package ru.itbasis.plugins.intellij.jvmwrapper

import com.intellij.openapi.components.ApplicationComponent
import ru.itbasis.plugins.intellij.jvmwrapper.services.application.ProjectSdkScannerApplicationService

class ProjectSdkScannerApplicationComponent(private val projectSdkScannerApplicationService: ProjectSdkScannerApplicationService) :
	ApplicationComponent {
	override fun initComponent() {
		projectSdkScannerApplicationService.run()
	}
}
