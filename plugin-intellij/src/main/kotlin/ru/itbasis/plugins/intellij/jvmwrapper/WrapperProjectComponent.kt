package ru.itbasis.plugins.intellij.jvmwrapper

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.AbstractProjectComponent
import com.intellij.openapi.externalSystem.service.project.IdeModifiableModelsProviderImpl
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.roots.LanguageLevelModuleExtensionImpl
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileContentsChangedAdapter
import com.intellij.openapi.vfs.VirtualFileListener
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.VirtualFilePropertyEvent
import ru.itbasis.jvmwrapper.core.JvmWrapper

class WrapperProjectComponent(
  project: Project, private val virtualFileManager: VirtualFileManager, private val projectSdkUpdater: ProjectSdkUpdater
) : AbstractProjectComponent(project) {

  private var jvmwWrapperListener: VirtualFileListener = object : VirtualFileContentsChangedAdapter() {
    override fun onFileChange(virtualFile: VirtualFile) = refresh(virtualFile)
    override fun propertyChanged(event: VirtualFilePropertyEvent) = refresh(event.file)

    override fun onBeforeFileChange(p0: VirtualFile) {}

    private fun refresh(virtualFile: VirtualFile) {
      if (virtualFile.nameWithoutExtension == JvmWrapper.SCRIPT_FILE_NAME) {
        projectSdkUpdater.update()
      }
    }
  }

  override fun projectOpened() = virtualFileManager.addVirtualFileListener(jvmwWrapperListener)

  override fun projectClosed() = virtualFileManager.removeVirtualFileListener(jvmwWrapperListener)

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
}
