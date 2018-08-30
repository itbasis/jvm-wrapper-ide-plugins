package ru.itbasis.plugins.intellij.jvmwrapper

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.roots.impl.LanguageLevelProjectExtensionImpl
import com.intellij.openapi.roots.impl.ProjectRootManagerImpl
import ru.itbasis.jvmwrapper.core.SystemInfo.isSupportedOS

class ProjectSdkUpdater(private val project: Project, private val jvmWrapperService: JvmWrapperService) {
  companion object {
    @JvmStatic
    fun getInstance(project: Project): ProjectSdkUpdater = ServiceManager.getService(project, ProjectSdkUpdater::class.java)
  }

  fun update() {
    if (!isSupportedOS) return

    if (!jvmWrapperService.hasWrapper()) return

    val wrapperSdk = jvmWrapperService.getSdk() ?: return

    ApplicationManager.getApplication().runWriteAction {
      (ProjectRootManager.getInstance(project) as ProjectRootManagerImpl).projectSdk = wrapperSdk
//
      LanguageLevelProjectExtensionImpl.getInstanceImpl(project).default = true
      LanguageLevelProjectExtensionImpl.MyProjectExtension(project).projectSdkChanged(wrapperSdk)
    }
  }
}
