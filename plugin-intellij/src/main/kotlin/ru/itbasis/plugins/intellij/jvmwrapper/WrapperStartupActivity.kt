package ru.itbasis.plugins.intellij.jvmwrapper

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class WrapperStartupActivity : StartupActivity {
  override fun runActivity(project: Project) = ServiceManager.getService(project, ProjectSdkUpdater::class.java).update()
}
