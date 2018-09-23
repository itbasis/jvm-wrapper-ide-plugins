package ru.itbasis.plugins.intellij.jvmwrapper

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.projectRoots.impl.JavaHomeFinder
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
		}.forEach { jvm ->
			SdkReceiver(sdkName = "system-$jvm", sdkPath = jvm.path!!, override = true).execute()
		}
	}

	companion object {
		@JvmStatic
		fun getInstance(): ProjectSdkScanner =
			ServiceManager.getService(ProjectSdkScanner::class.java)
	}
}
