package ru.itbasis.plugins.intellij.jvmwrapper.services.application

import com.intellij.openapi.projectRoots.JavaSdk
import com.intellij.openapi.projectRoots.ProjectJdkTable
import com.intellij.openapi.projectRoots.SdkType
import com.intellij.openapi.projectRoots.impl.JavaHomeFinder
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.jvmwrapper.core.jvm.toJvm
import ru.itbasis.jvmwrapper.core.wrapper.DEFAULT_JVMW_HOME_DIR
import ru.itbasis.jvmwrapper.core.wrapper.SCRIPT_FILE_NAME
import java.io.File
import java.nio.file.Paths

class ProjectSdkScannerApplicationService(
	private val sdkReceiverApplicationService: SdkReceiverApplicationService,
	private val projectJdkTable: ProjectJdkTable,
	private val javaSdk: JavaSdk
) : Runnable {
	override fun run() {
		val initJdkCount = projectJdkTable.allJdks.size

		val defaultSystemJvm = JavaHomeFinder.suggestHomePaths().map { path -> Paths.get(path) }

		val adoptOpenJdk = File("/usr/local/Cellar").takeIf { it.isDirectory }?.listFiles { _, name ->
			name.startsWith("adoptopenjdk-openjdk")
		}?.map { dir ->
			dir.listFiles().first().toPath()
		}
		                   ?: emptyList()
		val jvmwJvmDirs = DEFAULT_JVMW_HOME_DIR.takeIf { it.isDirectory }?.listFiles { dir ->
			dir?.isDirectory
			?: false
		}?.map { dir ->
			dir.toPath()
		}
		                  ?: emptyList()

		listOf(defaultSystemJvm, adoptOpenJdk).flatten().map { path ->
			path.toJvm(system = true)
		}.onEach { jvm ->
			sdkReceiverApplicationService.apply(sdkName = "system-$jvm", sdkPath = jvm.path!!, overrideAll = true)
		}

		listOf(jvmwJvmDirs).flatten().map { path ->
			Jvm.adjustPath(path).toJvm()
		}.onEach { jvm ->
			sdkReceiverApplicationService.apply(sdkName = "$SCRIPT_FILE_NAME-$jvm", sdkPath = jvm.path!!, overrideAll = true)
		}

		regenerateSuggestSdkName()
	}

	private fun regenerateSuggestSdkName() {
		val m = mutableMapOf<String, MutableList<Jvm>>()
		projectJdkTable.getSdksOfType(javaSdk).map { it ->
			it as ProjectJdkImpl
		}.filter {
			it.versionString != null
		}.forEach {
			val jvmHomePath = it.homePath
			val suggestSdkName = (it.sdkType as SdkType).suggestSdkName(it.sdkType.name, jvmHomePath)
			m.getOrPut(suggestSdkName) { mutableListOf() }.add(jvmHomePath.toJvm())
		}
		m.forEach { suggestSdkName, sdkList ->
			sdkList.sortDescending()
			sdkReceiverApplicationService.apply(sdkName = suggestSdkName, sdkPath = sdkList.first().path!!, overrideOnlyByName = true)
		}
	}
}
