package ru.itbasis.jvmwrapper.core.wrapper

import asRows
import io.kotlintest.Description
import io.kotlintest.TestResult
import io.kotlintest.data.forall
import io.kotlintest.matchers.file.startWithPath
import io.kotlintest.matchers.string.containIgnoringCase
import io.kotlintest.should
import io.kotlintest.shouldBe
import mu.KotlinLogging
import org.apache.commons.lang3.SystemUtils
import org.junit.rules.TemporaryFolder
import ru.itbasis.jvmwrapper.core.AbstractIntegrationTests
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.jvmwrapper.core.jvm.toJvmType
import ru.itbasis.jvmwrapper.core.jvm.toJvmVendor
import samples.JvmVersionSamples
import java.io.File
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

internal class JvmWrapperTest : AbstractIntegrationTests() {
	override val logger = KotlinLogging.logger {}

	private var temporaryFolder = TemporaryFolder()

	override fun beforeTest(description: Description) {
		temporaryFolder.create()
	}

	override fun afterTest(description: Description, result: TestResult) {
		temporaryFolder.delete()
	}

	private fun prepareTest(vendor: String, version: String) {
		logger.info { "temporaryFolder: ${temporaryFolder.root}" }
		logger.info { "vendor: $vendor, version: $version" }
		temporaryFolder.root.listFiles().forEach { it.deleteRecursively() }

		val propertiesFile = temporaryFolder.newFile("jvmw.properties").apply {
			appendText("JVM_VERSION=$version\n")
			appendText("JVM_VENDOR=$vendor")
		}
		logger.info { "--- properties file :: begin ---" }
		logger.info { propertiesFile.readText() }
		logger.info { "--- properties file :: end ---" }
		val workingDir = propertiesFile.parentFile
		workingDir.absolutePath shouldBe temporaryFolder.root.absolutePath

		File(System.getProperty("user.dir")).parentFile.resolve(SCRIPT_FILE_NAME).copyTo(File(workingDir, SCRIPT_FILE_NAME))
	}

	init {
		test("test all versions").config(enabled = SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC) {
			forall(
				rows = *JvmVersionSamples.asRows()
//				rows = *OpenJDKJvmVersionLatestSamples.asRows()
			) { (vendor, type, version, fullVersion, cleanVersion, _, _, _, _) ->
				prepareTest(vendor, version)

				val jvm = Jvm(
					vendor = vendor.toJvmVendor(), type = type.toJvmType(), version = version
				)
				val jvmWrapper = JvmWrapper(
					workingDir = temporaryFolder.root,
					stepListener = stepListener,
					downloadProcessListener = if (!launchedInCI()) downloadProcessListener else null
				)
				val jvmHomeDir = jvmWrapper.jvmHomeDir
				logger.info { "jvmHomeDir: $jvmHomeDir" }
				jvmHomeDir should startWithPath(JVMW_HOME_DIR.resolve(jvm.toString()))

				val jvmBinDir = jvmHomeDir.resolve("bin")
				logger.info { "jvmBinDir: $jvmBinDir" }
				jvmBinDir.exists() shouldBe true

				val process = ProcessBuilder(File(jvmBinDir, "java").absolutePath, "-fullversion").start()
				process.waitFor(5, TimeUnit.SECONDS)
				process.errorStream.readBytes().toString(Charset.defaultCharset()).trim() shouldBe containIgnoringCase(""" full version "$fullVersion"""")
			}
		}
	}
}
