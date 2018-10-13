package ru.itbasis.plugins.intellij.jvmwrapper

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileContentsChangedAdapter
import com.intellij.openapi.vfs.VirtualFileListener
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.VirtualFilePropertyEvent
import ru.itbasis.jvmwrapper.core.wrapper.SCRIPT_FILE_NAME
import ru.itbasis.plugins.intellij.jvmwrapper.services.project.SdkUpdaterProjectService

class WrapperProjectComponent(
	private val virtualFileManager: VirtualFileManager, private val sdkUpdaterProjectService: SdkUpdaterProjectService
) : ProjectComponent, Disposable {

	private var jvmwWrapperListener: VirtualFileListener = object : VirtualFileContentsChangedAdapter() {
		override fun onFileChange(virtualFile: VirtualFile) =
			refresh(virtualFile)

		override fun propertyChanged(event: VirtualFilePropertyEvent) =
			refresh(event.file)

		override fun onBeforeFileChange(p0: VirtualFile) {}

		private fun refresh(virtualFile: VirtualFile) {
			if (virtualFile.name.startsWith(SCRIPT_FILE_NAME)) {
				sdkUpdaterProjectService.run()
			}
		}
	}

	override fun projectOpened() {
		virtualFileManager.addVirtualFileListener(jvmwWrapperListener)
	}

	override fun projectClosed() {
		virtualFileManager.removeVirtualFileListener(jvmwWrapperListener)
	}

	override fun dispose() {
		virtualFileManager.removeVirtualFileListener(jvmwWrapperListener)
	}
}
