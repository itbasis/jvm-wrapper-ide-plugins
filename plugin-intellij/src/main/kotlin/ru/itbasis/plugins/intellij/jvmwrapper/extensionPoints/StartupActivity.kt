package ru.itbasis.plugins.intellij.jvmwrapper.extensionPoints

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import ru.itbasis.plugins.intellij.jvmwrapper.services.project.SdkUpdaterProjectService

class StartupActivity : StartupActivity {
	override fun runActivity(project: Project) {
		ServiceManager.getService(project, SdkUpdaterProjectService::class.java).run()
	}
}
