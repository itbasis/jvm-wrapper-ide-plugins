package ru.itbasis.plugins.intellij.jvmwrapper.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import ru.itbasis.plugins.intellij.jvmwrapper.ProjectSdkScanner
import ru.itbasis.plugins.intellij.jvmwrapper.ProjectSdkUpdater

class SdkUpdater : AnAction("Refresh"), StartupActivity {
	override fun runActivity(project: Project) {
		ProjectSdkScanner.getInstance().run()
		ProjectSdkUpdater.getInstance(project).run()
	}

	override fun actionPerformed(event: AnActionEvent) {
		runActivity(event.project!!)
	}
}
