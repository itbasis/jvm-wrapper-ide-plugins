package ru.itbasis.plugins.intellij.jvmwrapper.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.ServiceManager
import ru.itbasis.plugins.intellij.jvmwrapper.services.InvalidSdkCleanerService

class InvalidSdkCleanerAction : AnAction("Clean invalid Sdk") {
	override fun actionPerformed(event: AnActionEvent) {
		ServiceManager.getService(InvalidSdkCleanerService::class.java).startInWriteAction()
	}
}
