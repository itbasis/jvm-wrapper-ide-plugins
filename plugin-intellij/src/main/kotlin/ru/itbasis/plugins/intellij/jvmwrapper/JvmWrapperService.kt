package ru.itbasis.plugins.intellij.jvmwrapper

import com.intellij.execution.ExecutionException
import com.intellij.openapi.application.Result
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.JavaSdk
import com.intellij.openapi.projectRoots.ProjectJdkTable
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl
import com.intellij.openapi.projectRoots.impl.SdkConfigurationUtil
import com.twelvemonkeys.io.FileUtil
import ru.itbasis.jvmwrapper.core.JvmWrapper
import ru.itbasis.jvmwrapper.core.ProcessStepListener
import ru.itbasis.jvmwrapper.core.SystemInfo.isSupportedOS
import ru.itbasis.jvmwrapper.core.vendor.DownloadProcessListener
import java.io.File

class JvmWrapperService(
  private val project: Project,
  private val javaSdk: JavaSdk,
  private val projectJdkTable: ProjectJdkTable,
  private val progressManager: ProgressManager
) {
  companion object {
    @JvmStatic
    fun getInstance(project: Project): JvmWrapperService {
      return ServiceManager.getService(project, JvmWrapperService::class.java)
    }
  }

  fun hasWrapper(): Boolean {
    return isSupportedOS && project.baseDir.findFileByRelativePath(JvmWrapper.SCRIPT_FILE_NAME)?.exists() ?: false
  }

  private fun getWrapper(): JvmWrapper? {
    return progressManager.run(object : Task.WithResult<JvmWrapper, ExecutionException>(project, "JVM Wrapper", true) {
      override fun compute(progressIndicator: ProgressIndicator): JvmWrapper? {
        return if (!hasWrapper()) null
        else JvmWrapper(
          workingDir = File(project.baseDir.canonicalPath),
          stepListener = stepListener(progressIndicator),
          downloadProcessListener = downloadProcessListener(progressIndicator)
        )
      }
    })
  }

  fun getSdk(): Sdk? {
    return actionGetSdk.execute().resultObject
  }

  private val actionGetSdk = object : WriteAction<Sdk?>() {
    override fun run(result: Result<Sdk?>) {
      val wrapper = getWrapper() ?: return

      val sdkName = "${JvmWrapper.SCRIPT_FILE_NAME}-${wrapper.jvmName}"

      var findJdk = projectJdkTable.findJdk(sdkName)
      while (findJdk != null) {
        projectJdkTable.removeJdk(findJdk)
        findJdk = projectJdkTable.findJdk(sdkName)
      }
//
      val sdk = SdkConfigurationUtil.createAndAddSDK(wrapper.jvmHomeDir.absolutePath, javaSdk) as ProjectJdkImpl
      val wrapperSdk = sdk.clone().apply { name = sdkName }
      projectJdkTable.updateJdk(sdk, wrapperSdk)
      result.setResult(wrapperSdk)
    }
  }

  private fun stepListener(progressIndicator: ProgressIndicator): ProcessStepListener = { msg ->
    ProgressManager.checkCanceled()

    progressIndicator.run {
      fraction = 0.0
      text = msg
    }
  }

  private fun downloadProcessListener(progressIndicator: ProgressIndicator): DownloadProcessListener = { url, sizeCurrent, sizeTotal ->
    progressIndicator.run {
      ProgressManager.checkCanceled()
      val percentage = sizeCurrent.toDouble() / sizeTotal
      fraction = percentage
      text = "%s / %s (%.2f%%) < %s".format(
        FileUtil.toHumanReadableSize(sizeCurrent), FileUtil.toHumanReadableSize(sizeTotal), percentage * 100, url
      )
      return@run this.isRunning
    }
  }
}