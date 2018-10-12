package ru.itbasis.plugins.intellij.jvmwrapper.extensionPoints

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import ru.itbasis.plugins.intellij.jvmwrapper.ProjectSdkScanner
import ru.itbasis.plugins.intellij.jvmwrapper.ProjectSdkUpdater

class StartupActivity : StartupActivity {
	override fun runActivity(project: Project) {
		ProjectSdkScanner.getInstance().run()
		ProjectSdkUpdater.getInstance(project).run()
	}
}
