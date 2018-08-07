package ru.itbasis.plugins.intellij.jvmwrapper

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project

class JvmWrapperTask(project: Project) : Task.WithResult<JvmWrapperPlugin, Exception>(project, "JVM Wrapper", true) {
  override fun compute(p0: ProgressIndicator): JvmWrapperPlugin {
    return JvmWrapperPlugin(project, p0)
  }
}
