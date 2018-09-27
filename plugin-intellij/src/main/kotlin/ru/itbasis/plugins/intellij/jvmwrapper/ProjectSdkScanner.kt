package ru.itbasis.plugins.intellij.jvmwrapper

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.projectRoots.JavaSdk
import com.intellij.openapi.projectRoots.ProjectJdkTable
import com.intellij.openapi.projectRoots.SdkType
import com.intellij.openapi.projectRoots.impl.JavaHomeFinder
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.jvmwrapper.core.jvm.toJvm
import ru.itbasis.jvmwrapper.core.wrapper.JVMW_HOME_DIR
import ru.itbasis.jvmwrapper.core.wrapper.SCRIPT_FILE_NAME
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
		val jvmwJvmDirs = JVMW_HOME_DIR.listFiles { dir ->
			dir?.isDirectory
			?: false
		}.map { dir ->
			dir.toPath()
		}

		listOf(defaultSystemJvm, adoptOpenJdk).flatten().map { path ->
			path.toJvm(system = true)
		}.onEach { jvm ->
			SdkReceiver(sdkName = "system-$jvm", sdkPath = jvm.path!!, overrideAll = true).execute()
		}

		listOf(jvmwJvmDirs).flatten().map { path ->
			Jvm.adjustPath(path).toJvm()
		}.onEach { jvm ->
			SdkReceiver(sdkName = "$SCRIPT_FILE_NAME-$jvm", sdkPath = jvm.path!!, overrideAll = true).execute()
		}

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
			SdkReceiver(sdkName = suggestSdkName, sdkPath = sdkList.first().path!!, overrideOnlyByName = true).execute()
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
