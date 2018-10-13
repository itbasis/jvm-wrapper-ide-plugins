package ru.itbasis.plugins.intellij.jvmwrapper.services.application

import com.intellij.openapi.application.Application
import com.intellij.openapi.projectRoots.JavaSdk
import com.intellij.openapi.projectRoots.ProjectJdkTable
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.projectRoots.SdkType
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl
import java.nio.file.Path

class SdkReceiverApplicationService(private val application: Application, private val projectJdkTable: ProjectJdkTable, private val javaSdk: JavaSdk) {
	fun apply(
		sdkName: String,
		sdkPath: Path,
		overrideAll: Boolean = false,
		overrideOnlyByName: Boolean = overrideAll,
		overrideOnlyByPath: Boolean = overrideAll
	): Sdk {
		return application.runWriteAction<Sdk> {
			val sdkHome = sdkPath.toFile().absolutePath

			val sdkList = projectJdkTable.getSdksOfType(javaSdk)
			val sdkFilteredList = when {
				overrideOnlyByName && !overrideOnlyByPath -> sdkList.filter {
					sdkName == it.name
				}
				!overrideOnlyByName && overrideOnlyByPath -> sdkList.filter {
					sdkHome == it.homePath
				}
				else                                      -> sdkList.filter {
					sdkName == it.name || sdkHome == it.homePath
				}
			}
			sdkFilteredList.takeIf {
				it.size > 1
			}?.forEach {
				projectJdkTable.removeJdk(it)
			}

			return@runWriteAction projectJdkTable.findJdk(sdkName, javaSdk.name)
			                      ?: run {
				                      val projectJdk = ProjectJdkImpl(sdkName, javaSdk, sdkHome, javaSdk.getVersionString(sdkHome))
				                      val sdkType = projectJdk.sdkType as SdkType
				                      sdkType.setupSdkPaths(projectJdk)
				                      projectJdkTable.addJdk(projectJdk)
				                      projectJdk
			                      }
		}
	}
}
