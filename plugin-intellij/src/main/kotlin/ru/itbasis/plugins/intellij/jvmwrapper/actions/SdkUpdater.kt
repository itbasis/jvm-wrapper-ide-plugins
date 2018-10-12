package ru.itbasis.plugins.intellij.jvmwrapper.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ru.itbasis.plugins.intellij.jvmwrapper.ProjectSdkScanner
import ru.itbasis.plugins.intellij.jvmwrapper.ProjectSdkUpdater

class SdkUpdater : AnAction("Refresh") {
	override fun actionPerformed(event: AnActionEvent) {
		ProjectSdkScanner.getInstance().run()
		ProjectSdkUpdater.getInstance(event.project!!).run()
	}
}
