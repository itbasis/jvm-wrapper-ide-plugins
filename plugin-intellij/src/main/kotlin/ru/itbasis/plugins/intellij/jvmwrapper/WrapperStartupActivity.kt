package ru.itbasis.plugins.intellij.jvmwrapper

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class WrapperStartupActivity : StartupActivity {
  override fun runActivity(project: Project) {
    ProjectSdkScanner.getInstance().run()
    ProjectSdkUpdater.getInstance(project).run()
  }
}
