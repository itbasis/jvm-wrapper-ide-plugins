@file:Suppress("Destructure")

package ru.itbasis.jvmwrapper.core.wrapper

import io.kotlintest.Description
import io.kotlintest.TestResult
import io.kotlintest.data.forall
import io.kotlintest.matchers.file.startWithPath
import io.kotlintest.matchers.string.containIgnoringCase
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.tables.row
import mu.KotlinLogging
import org.junit.rules.TemporaryFolder
import ru.itbasis.jvmwrapper.core.AbstractIntegrationTests
import ru.itbasis.jvmwrapper.core.downloader.downloader
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.jvmwrapper.core.jvm.fixFromMac
import ru.itbasis.jvmwrapper.core.jvm.toJvmType
import ru.itbasis.jvmwrapper.core.jvm.toJvmVendor
import ru.itbasis.jvmwrapper.core.unarchiver.UnarchiverFactory
import samples.JvmVersionRow
import samples.asJvmVersionRow
import samples.jvmVersionSample__openjdk_jdk_11
import samples.jvmVersionSample__openjdk_jdk_11_0_1
import java.io.File

internal class JvmWrapperTest : AbstractIntegrationTests() {
	override val logger = KotlinLogging.logger {}

	override fun isInstancePerTest(): Boolean {
		return false
	}

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

//		JVMW_HOME_DIR.deleteRecursively()
		temporaryFolder.root.listFiles().forEach {
			it.deleteRecursively()
		}

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

	private fun buildPreviousVersion(jvmVersionSample: JvmVersionRow): Jvm {
		val version = jvmVersionSample.version
		val vendor = jvmVersionSample.vendor
		val type = jvmVersionSample.type

		prepareTest(vendor, version)

		logger.info { "version: $version" }
		val jvm = Jvm(vendor = vendor.toJvmVendor(), type = type.toJvmType(), version = version)
		logger.info { "jvm: $jvm" }
		val downloader = jvm.downloader()

		val jvmHomePath = File(JVMW_HOME_DIR, jvm.toString())

		val tempFile = downloader.downloadToTempFile(
			remoteArchiveFile = jvmVersionSample.remoteFiles.getValue(jvm.os), archiveFileExtension = jvm.archiveFileExtension
		)
		UnarchiverFactory.getInstance(sourceFile = tempFile, targetDir = jvmHomePath).unpack()

		val actualFullVersion = getFullVersion(jvmHomePath = jvmHomePath.toPath().fixFromMac())
		actualFullVersion shouldBe containIgnoringCase(""" full version "${jvmVersionSample.fullVersion}"""")

		return jvm
	}

	private fun testJvmVersion(vendor: String, type: String, version: String, versionMajor: Int, fullVersion: String) {
		prepareTest(vendor, version)

		val jvm = Jvm(
			vendor = vendor.toJvmVendor(), type = type.toJvmType(), version = version
		)
		val jvmWrapper = JvmWrapper(
			workingDir = temporaryFolder.root, stepListener = stepListener, downloadProcessListener = downloadProcessListener
		)
		val jvmHomeDir = jvmWrapper.jvmHomeDir
		logger.info { "jvmHomeDir: $jvmHomeDir" }
		jvmHomeDir should startWithPath(JVMW_HOME_DIR.resolve(jvm.toString()))

		val actualFullVersion = getFullVersion(jvmHomePath = jvmHomeDir.toPath())
		actualFullVersion shouldBe containIgnoringCase(""" full version "$fullVersion"""")
	}

	init {
		test("update jvm").config(enabled = isNixOS) {
			forall(
				row(jvmVersionSample__openjdk_jdk_11.asJvmVersionRow().first(), jvmVersionSample__openjdk_jdk_11_0_1.asJvmVersionRow().first())
			) { previousJvmVersionSample, jvmVersionSample ->
				val previousJvmVersion = buildPreviousVersion(previousJvmVersionSample)
				val lastUpdateFile = LastUpdateFile(jvm = previousJvmVersion).file
				lastUpdateFile.delete()
				File(lastUpdateFile.absolutePath.substringBefore(LastUpdateFile.FILE_EXTENSION) + ".${previousJvmVersion.archiveFileExtension}").takeIf { it.exists() }
					?.delete()

				testJvmVersion(
					vendor = jvmVersionSample.vendor,
					type = jvmVersionSample.type,
					version = jvmVersionSample.version,
					versionMajor = jvmVersionSample.versionMajor,
					fullVersion = jvmVersionSample.fullVersion
				)
			}
		}

		test("test all versions").config(enabled = isNixOS) {
			//		test("test all versions").config(enabled = false) {
			forall(
				rows = *jvmAllRows
			) { (vendor, type, version, fullVersion, _, versionMajor) ->
				testJvmVersion(
					vendor = vendor, type = type, version = version, versionMajor = versionMajor, fullVersion = fullVersion
				)
			}
		}
	}
}
