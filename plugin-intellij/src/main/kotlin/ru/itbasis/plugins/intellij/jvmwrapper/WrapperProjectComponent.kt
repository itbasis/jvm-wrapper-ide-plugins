package ru.itbasis.plugins.intellij.jvmwrapper

import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileContentsChangedAdapter
import com.intellij.openapi.vfs.VirtualFileListener
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.VirtualFilePropertyEvent
import ru.itbasis.jvmwrapper.core.wrapper.SCRIPT_FILE_NAME

class WrapperProjectComponent(
	private val project: Project, private val virtualFileManager: VirtualFileManager, private val projectSdkUpdater: ProjectSdkUpdater
) : ProjectComponent {

	private var jvmwWrapperListener: VirtualFileListener = object : VirtualFileContentsChangedAdapter() {
		override fun onFileChange(virtualFile: VirtualFile) =
			refresh(virtualFile)

		override fun propertyChanged(event: VirtualFilePropertyEvent) =
			refresh(event.file)

		override fun onBeforeFileChange(p0: VirtualFile) {}

		private fun refresh(virtualFile: VirtualFile) {
			if (virtualFile.name.startsWith(SCRIPT_FILE_NAME)) {
				projectSdkUpdater.run()
			}
		}
	}

	override fun projectOpened() {
		virtualFileManager.addVirtualFileListener(jvmwWrapperListener)
	}

	override fun projectClosed() {
		virtualFileManager.removeVirtualFileListener(jvmwWrapperListener)
	}

	// FIXME
//	private fun updateModulesSdk(wrapperSdk: Sdk) {
//		ApplicationManager.getApplication().runWriteAction {
//			val moduleModel = ModuleManager.getInstance(project).modifiableModel
//			moduleModel.apply {
//				modules.forEach { module ->
//					IdeModifiableModelsProviderImpl(project).getModifiableRootModel(module).apply {
//						sdk = wrapperSdk
//						inheritSdk()
//						commit()
//					}
////
//					(LanguageLevelModuleExtensionImpl.getInstance(module).getModifiableModel(true) as LanguageLevelModuleExtensionImpl).apply {
//						languageLevel = null
//						commit()
//					}
//				}
//				commit()
//			}
//		}
//	}
}
