import org.gradle.plugins.ide.idea.model.IdeaModel

val kotlinVersion: String by extra

repositories {
	jcenter()
	mavenCentral()
	maven(url = "https://jitpack.io")
	maven(url = "https://dl.bintray.com/kotlin/ktor")
	maven(url = "https://kotlin.bintray.com/kotlinx")
	mavenLocal()
}

configurations.all {
	resolutionStrategy {
		failOnVersionConflict()

		eachDependency {
			when (requested.group) {
				"org.slf4j"                       -> useVersion("+")
				"junit"                           -> useVersion("+")
				"org.junit.platform"              -> useVersion("+")
				"org.junit.jupiter"               -> useVersion("+")
				"org.mockito"                     -> useVersion("+")
				"org.opentest4j"                  -> useVersion("+")
				"org.objenesis"                   -> useVersion("+")
				"org.jetbrains.kotlin"            -> useVersion(kotlinVersion)
//				"org.jetbrains.kotlinx"           -> when {
//					requested.name.startsWith("kotlinx-io")                -> useVersion("0.1.0-alpha-9-eap13")
//					requested.name.startsWith("kotlinx.atomicfu-")         -> useVersion("0.11.9-eap13")
//					requested.name.startsWith("kotlinx-coroutines-io-jvm") -> useVersion("0.1.0-alpha-9-eap13")
//					requested.name.startsWith("kotlinx-coroutines-io")     -> useVersion("0.24.0-eap13")
//					requested.name.startsWith("kotlinx-coroutines-")       -> useVersion("0.30.1-eap13")
//					else                                                   -> useVersion("+")
//				}
				"io.kotlintest"                   -> useVersion("+")
				"io.ktor"                         -> useVersion("+")
				"io.github.glytching"             -> useVersion("+")
				"org.apache.commons"              -> when (requested.name) {
					"commons-lang3"    -> useVersion("+")
					"commons-compress" -> useVersion("+")
				}
				"org.apache.httpcomponents"       -> when (requested.name) {
					"httpclient" -> useVersion("+")
					"httpcore"   -> useVersion("+")
				}
				"com.google.code.gson"            -> useVersion("+")
				"io.github.microutils"            -> useVersion("+")
				"com.github.itbasis.kotlin-utils" -> useVersion("v20180618_1139")
				"org.codehaus.plexus"             -> useVersion("+")
			}
		}
	}
}

apply {
	plugin<IdeaPlugin>()
}

configure<IdeaModel> {
	module {
		isDownloadJavadoc = false
		isDownloadSources = false
	}
}
