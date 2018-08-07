package ru.itbasis.plugins.intellij.jvmwrapper.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ru.itbasis.jvmwrapper.core.actions.GenerateWrapperAction
import ru.itbasis.plugins.intellij.jvmwrapper.JvmWrapperService
import java.io.File

class GenerateAction : AnAction("Generate") {
  override fun update(event: AnActionEvent) {
    event.presentation.isEnabledAndVisible = !JvmWrapperService.getInstance(event.project!!).hasWrapper()
  }

  override fun actionPerformed(event: AnActionEvent) {
    GenerateWrapperAction(
      parentDir = File(event.project!!.baseDir.canonicalPath)
    ).run()
  }
}
