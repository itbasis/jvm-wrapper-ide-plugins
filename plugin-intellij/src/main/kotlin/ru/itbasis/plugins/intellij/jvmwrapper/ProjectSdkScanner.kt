package ru.itbasis.plugins.intellij.jvmwrapper

import com.intellij.openapi.components.ServiceManager
import ru.itbasis.jvmwrapper.core.system.SystemJvmScanner
import ru.itbasis.plugins.intellij.jvmwrapper.actions.SdkReceiver

class ProjectSdkScanner : Runnable {
  override fun run() {
    SystemJvmScanner.getInstance().listJvm().forEach { jvm ->
      val sdkName = (if (jvm.system) "system" else "manual") + "-" + jvm.toString()
      SdkReceiver(sdkName = sdkName, sdkPath = jvm.path!!).execute()
    }
  }

  companion object {
    @JvmStatic
    fun getInstance(): ProjectSdkScanner = ServiceManager.getService(ProjectSdkScanner::class.java)
  }
}
