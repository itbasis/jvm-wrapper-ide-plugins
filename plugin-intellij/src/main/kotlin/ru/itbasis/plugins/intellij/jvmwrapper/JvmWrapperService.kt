package ru.itbasis.plugins.intellij.jvmwrapper

import com.intellij.execution.ExecutionException
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.util.io.exists
import com.twelvemonkeys.io.FileUtil
import ru.itbasis.jvmwrapper.core.ProcessStepListener
import ru.itbasis.jvmwrapper.core.SystemInfo.isSupportedOS
import ru.itbasis.jvmwrapper.core.downloader.DownloadProcessListener
import ru.itbasis.jvmwrapper.core.jvm.getExecutable
import ru.itbasis.jvmwrapper.core.wrapper.JvmWrapper
import ru.itbasis.jvmwrapper.core.wrapper.SCRIPT_FILE_NAME
import ru.itbasis.plugins.intellij.jvmwrapper.actions.SdkReceiver
import java.io.File
import java.nio.file.Paths

class JvmWrapperService(
	private val project: Project, private val progressManager: ProgressManager
) {
	companion object {
		@JvmStatic
		fun getInstance(project: Project): JvmWrapperService {
			return ServiceManager.getService(project, JvmWrapperService::class.java)
		}
	}

	fun hasWrapper(): Boolean {
		return isSupportedOS && Paths.get(project.basePath).getExecutable(SCRIPT_FILE_NAME).exists()
	}

	private fun getWrapper(): JvmWrapper? {
		return progressManager.run(object : Task.WithResult<JvmWrapper, ExecutionException>(project, "JRE Wrapper", true) {
			override fun compute(progressIndicator: ProgressIndicator): JvmWrapper? {
				return if (!hasWrapper()) null
				else JvmWrapper(
					workingDir = File(project.basePath),
					stepListener = stepListener(progressIndicator),
					downloadProcessListener = downloadProcessListener(progressIndicator)
				)
			}
		})
	}

	fun getSdk(): Sdk? {
		val wrapper = getWrapper()
		              ?: return null
		return SdkReceiver(sdkName = wrapper.sdkName, sdkPath = wrapper.jvmHomeDir.toPath(), overrideAll = true).execute().resultObject
	}

	private fun stepListener(progressIndicator: ProgressIndicator): ProcessStepListener =
		{ msg ->
			ProgressManager.checkCanceled()

			progressIndicator.run {
				fraction = 0.0
				text = msg
			}
		}

	private fun downloadProcessListener(progressIndicator: ProgressIndicator): DownloadProcessListener =
		{ url, sizeCurrent, sizeTotal ->
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