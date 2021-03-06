package ru.itbasis.plugins.intellij.jvmwrapper.services.project

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.roots.impl.LanguageLevelProjectExtensionImpl
import com.intellij.openapi.roots.impl.ProjectRootManagerImpl
import ru.itbasis.jvmwrapper.core.SystemInfo.isSupportedOS

class SdkUpdaterProjectService(private val project: Project, private val jvmWrapperProjectService: JvmWrapperProjectService) : Runnable {
	override fun run() {
		if (!isSupportedOS) return

		if (!jvmWrapperProjectService.hasWrapper()) return

		val wrapperSdk = jvmWrapperProjectService.getSdk()
		                 ?: return

		ApplicationManager.getApplication().runWriteAction {
			(ProjectRootManager.getInstance(project) as ProjectRootManagerImpl).projectSdk = wrapperSdk
//
			LanguageLevelProjectExtensionImpl.getInstanceImpl(project).default = true
			LanguageLevelProjectExtensionImpl.MyProjectExtension(project).projectSdkChanged(wrapperSdk)
		}
	}
}
