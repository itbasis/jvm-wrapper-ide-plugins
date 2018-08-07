package ru.itbasis.plugins.intellij.jvmwrapper.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ru.itbasis.plugins.intellij.jvmwrapper.JvmWrapperService
import ru.itbasis.plugins.intellij.jvmwrapper.ProjectSdkUpdater

class JvmUpdaterAction : AnAction("Refresh") {
  override fun update(event: AnActionEvent) {
    event.presentation.isEnabledAndVisible = JvmWrapperService.getInstance(event.project!!).hasWrapper()
  }

  override fun actionPerformed(event: AnActionEvent) = ProjectSdkUpdater.getInstance(event.project!!).update()
}
