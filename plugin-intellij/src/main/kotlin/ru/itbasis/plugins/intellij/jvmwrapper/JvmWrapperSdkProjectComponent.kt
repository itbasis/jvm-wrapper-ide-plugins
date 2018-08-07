package ru.itbasis.plugins.intellij.jvmwrapper

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.Result
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.components.AbstractProjectComponent
import com.intellij.openapi.externalSystem.service.project.IdeModifiableModelsProviderImpl
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.JavaSdk
import com.intellij.openapi.projectRoots.ProjectJdkTable
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl
import com.intellij.openapi.projectRoots.impl.SdkConfigurationUtil
import com.intellij.openapi.roots.LanguageLevelModuleExtensionImpl
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.roots.impl.LanguageLevelProjectExtensionImpl
import com.intellij.openapi.roots.impl.ProjectRootManagerImpl
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileContentsChangedAdapter
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.VirtualFilePropertyEvent
import ru.itbasis.jvmwrapper.core.JvmWrapper
import java.io.File

class JvmWrapperSdkProjectComponent(
  project: Project,
  private val javaSdk: JavaSdk,
  private val projectJdkTable: ProjectJdkTable,
  private val virtualFileManager: VirtualFileManager,
  private val progressManager: ProgressManager
) : AbstractProjectComponent(project) {

  private var jvmwWrapperListener: VirtualFileContentsChangedAdapter = object : VirtualFileContentsChangedAdapter() {
    override fun onFileChange(virtualFile: VirtualFile) {
      if (virtualFile.nameWithoutExtension == JvmWrapper.SCRIPT_FILE_NAME) readAndUpdateWrapperSdk()
    }

    override fun propertyChanged(event: VirtualFilePropertyEvent) {
      super.propertyChanged(event)
      if (event.file.nameWithoutExtension == JvmWrapper.SCRIPT_FILE_NAME) readAndUpdateWrapperSdk()
    }

    override fun onBeforeFileChange(virtualFile: VirtualFile) {}
  }

  override fun projectOpened() {
    // FIXME https://github.com/itbasis/jvm-wrapper/issues/38
//    readAndUpdateWrapperSdk()
    virtualFileManager.addVirtualFileListener(jvmwWrapperListener)
  }

  override fun projectClosed() {
    virtualFileManager.removeVirtualFileListener(jvmwWrapperListener)
  }

  private fun readAndUpdateWrapperSdk() {
    readWrapperSdk()?.let { updateProjectSdk(it) }
//    updateModulesSdk(wrapperSdk)
  }

  private fun updateModulesSdk(wrapperSdk: Sdk) {
    ApplicationManager.getApplication().runWriteAction {
      val moduleModel = ModuleManager.getInstance(myProject).modifiableModel
      moduleModel.apply {
        modules.forEach { module ->
          IdeModifiableModelsProviderImpl(myProject).getModifiableRootModel(module).apply {
            sdk = wrapperSdk
            inheritSdk()
            commit()
          }
//
          (LanguageLevelModuleExtensionImpl.getInstance(module).getModifiableModel(true) as LanguageLevelModuleExtensionImpl).apply {
            languageLevel = null
            commit()
          }
        }
        commit()
      }
    }
  }

  private fun readWrapperSdk(): Sdk? {
    val ideaFolder = File(myProject.baseDir.canonicalPath).resolve(".idea")
    val jvmWrapperScript = ideaFolder.resolveSibling(JvmWrapper.SCRIPT_FILE_NAME)
    if (!jvmWrapperScript.exists()) return null

    val jvmWrapper = progressManager.run(JvmWrapperTask(myProject))

    return object : WriteAction<Sdk>() {
      override fun run(p0: Result<Sdk>) {
        var findJdk = projectJdkTable.findJdk(jvmWrapper.sdkName)
        while (findJdk != null) {
          projectJdkTable.removeJdk(findJdk)
          findJdk = projectJdkTable.findJdk(jvmWrapper.sdkName)
        }
//
        val sdk = SdkConfigurationUtil.createAndAddSDK(jvmWrapper.jvmHomeDir.absolutePath, javaSdk) as ProjectJdkImpl
        val wrapperSdk = sdk.clone().apply { name = jvmWrapper.sdkName }
        projectJdkTable.updateJdk(sdk, wrapperSdk)
        p0.setResult(wrapperSdk)
      }
    }.execute().resultObject
  }

  private fun updateProjectSdk(wrapperSdk: Sdk) {
    ApplicationManager.getApplication().runWriteAction {
      (ProjectRootManager.getInstance(myProject) as ProjectRootManagerImpl).projectSdk = wrapperSdk
//
      LanguageLevelProjectExtensionImpl.getInstanceImpl(myProject).default = true
      LanguageLevelProjectExtensionImpl.MyProjectExtension(myProject).projectSdkChanged(wrapperSdk)
    }
  }
}
