package ru.itbasis.plugins.intellij.jvmwrapper.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.ServiceManager
import ru.itbasis.jvmwrapper.core.actions.GenerateWrapperAction
import ru.itbasis.plugins.intellij.jvmwrapper.services.project.JvmWrapperProjectService
import java.io.File

class GenerateAction : AnAction("Generate") {
	override fun update(event: AnActionEvent) {
		event.presentation.isEnabledAndVisible = !ServiceManager.getService(event.project!!, JvmWrapperProjectService::class.java).hasWrapper()
	}

	override fun actionPerformed(event: AnActionEvent) {
		GenerateWrapperAction(
			parentDir = File(event.project!!.basePath)
		).run()
	}
}
