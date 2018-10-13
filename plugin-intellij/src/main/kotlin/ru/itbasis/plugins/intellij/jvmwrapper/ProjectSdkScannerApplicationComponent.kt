package ru.itbasis.plugins.intellij.jvmwrapper

import com.intellij.openapi.components.ApplicationComponent
import com.intellij.openapi.components.ServiceManager
import ru.itbasis.plugins.intellij.jvmwrapper.services.application.ProjectSdkScannerApplicationService

class ProjectSdkScannerApplicationComponent : ApplicationComponent {
	override fun initComponent() {
		ServiceManager.getService(ProjectSdkScannerApplicationService::class.java).run()
	}
}
