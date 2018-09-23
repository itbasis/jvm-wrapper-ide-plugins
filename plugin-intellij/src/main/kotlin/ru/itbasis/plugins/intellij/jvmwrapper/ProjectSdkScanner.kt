package ru.itbasis.plugins.intellij.jvmwrapper

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.projectRoots.JavaSdk
import com.intellij.openapi.projectRoots.JavaSdkVersion
import com.intellij.openapi.projectRoots.ProjectJdkTable
import com.intellij.openapi.projectRoots.impl.JavaHomeFinder
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.plugins.intellij.jvmwrapper.actions.SdkReceiver
import java.io.File
import java.nio.file.Paths

class ProjectSdkScanner : Runnable {
	override fun run() {
		val defaultSystemJvm = JavaHomeFinder.suggestHomePaths().map { path -> Paths.get(path) }

		val adoptOpenJdk = File("/usr/local/Cellar").listFiles { _, name ->
			name.startsWith("adoptopenjdk-openjdk")
		}.map { dir ->
			dir.listFiles().first().toPath()
		}

		listOf(defaultSystemJvm, adoptOpenJdk).flatten().map { path ->
			Jvm(system = true, path = Jvm.adjustPath(path))
		}.onEach { jvm ->
			SdkReceiver(sdkName = "system-$jvm", sdkPath = jvm.path!!, overrideOnlyByName = true).execute()
		}

		projectJdkTable.getSdksOfType(javaSdk).map { it -> it as ProjectJdkImpl }.filter {
			it.versionString != null
		}.associateBy({ JavaSdkVersion.fromVersionString(it.versionString!!) }, { it }).filterKeys {
			it != null
		}.forEach {
			SdkReceiver(sdkName = it.key!!.description, sdkPath = Paths.get(it.value.homePath), overrideOnlyByName = true).execute()
		}
	}

	companion object {
		@JvmStatic
		fun getInstance(): ProjectSdkScanner {
			return ServiceManager.getService(ProjectSdkScanner::class.java)
		}

		private val projectJdkTable = ServiceManager.getService(ProjectJdkTable::class.java)
		private val javaSdk = JavaSdk.getInstance()
	}
}
