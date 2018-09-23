package ru.itbasis.plugins.intellij.jvmwrapper.actions

import com.intellij.openapi.application.Result
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.projectRoots.JavaSdk
import com.intellij.openapi.projectRoots.ProjectJdkTable
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.projectRoots.SdkType
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl
import java.nio.file.Path

class SdkReceiver(
	private val sdkName: String,
	private val sdkPath: Path,
	private val overrideAll: Boolean = false,
	private val overrideOnlyByName: Boolean = overrideAll,
	private val overrideOnlyByPath: Boolean = overrideAll
) : WriteAction<Sdk>() {
	override fun run(result: Result<Sdk>) {
		val sdkHome = sdkPath.toFile().absolutePath

		projectJdkTable.allJdks.filter {
			(overrideOnlyByName && sdkName == it.name) || (overrideOnlyByPath && sdkHome == it.homePath)
		}.forEach {
			projectJdkTable.removeJdk(it)
		}

		val sdk = projectJdkTable.findJdk(sdkName, javaSdk.name)
		          ?: run {
			          val projectJdk = ProjectJdkImpl(sdkName, javaSdk, sdkHome, javaSdk.getVersionString(sdkHome))
			          val sdkType = projectJdk.sdkType as SdkType
			          sdkType.setupSdkPaths(projectJdk)
			          projectJdkTable.addJdk(projectJdk)
			          projectJdk
		          }
		result.setResult(sdk)
	}

	companion object {
		private val projectJdkTable = ServiceManager.getService(ProjectJdkTable::class.java)
		private val javaSdk = JavaSdk.getInstance()
	}
}
