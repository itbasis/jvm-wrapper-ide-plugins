package ru.itbasis.jvmwrapper.core

import io.kotlintest.Description
import io.kotlintest.TestResult
import io.kotlintest.matchers.string.containIgnoringCase
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec
import mu.KLogger
import org.apache.commons.lang3.SystemUtils
import org.junit.rules.TemporaryFolder
import ru.itbasis.jvmwrapper.core.downloader.AbstractDownloader
import ru.itbasis.jvmwrapper.core.downloader.DownloadProcessListener
import ru.itbasis.jvmwrapper.core.downloader.RemoteArchiveFile
import ru.itbasis.jvmwrapper.core.downloader.downloader
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.jvmwrapper.core.jvm.fixFromMac
import ru.itbasis.jvmwrapper.core.jvm.toJvmType
import ru.itbasis.jvmwrapper.core.jvm.toJvmVendor
import ru.itbasis.jvmwrapper.core.unarchiver.UnarchiverFactory
import ru.itbasis.jvmwrapper.core.wrapper.JvmWrapperPropertyKeys
import ru.itbasis.jvmwrapper.core.wrapper.SCRIPT_FILE_NAME
import samples.JvmVersionRow
import samples.OpenJDKJvmVersionEarlyAccessSamples
import samples.OpenJDKJvmVersionLatestSamples
import samples.OracleJvmVersionArchiveSamples
import samples.OracleJvmVersionLatestSamples
import samples.asKotlinTestRows
import samples.jvmVersionSample__openjdk_jdk_11
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.charset.Charset
import java.nio.file.Path
import java.util.concurrent.TimeUnit

abstract class AbstractIntegrationTests : FunSpec() {
	abstract val logger: KLogger

	protected open val jvmAllRows = listOf(
		jvmVersionSample__openjdk_jdk_11
	).asKotlinTestRows()
//	protected open val jvmAllRows = listOf(
//		OpenJDKJvmVersionLatestSamples, OracleJvmVersionLatestSamples, OracleJvmVersionArchiveSamples, OpenJDKJvmVersionEarlyAccessSamples
//	).flatten().samples.asKotlinTestRows()

	protected open val jvmFirstRows = listOf(
		OpenJDKJvmVersionLatestSamples.firstOrNull(),
		OracleJvmVersionLatestSamples.firstOrNull(),
		OracleJvmVersionArchiveSamples.firstOrNull(),
		OpenJDKJvmVersionEarlyAccessSamples.firstOrNull()
	).asKotlinTestRows()

	override fun isInstancePerTest(): Boolean {
		return true
	}

	protected val stepListener: (String) -> Unit = { msg ->
		logger.info { msg }
	}

	protected val downloadProcessListener: DownloadProcessListener? = if (launchedInCI) {
		null
	} else {
		{ _, sizeCurrent, sizeTotal ->
			val percentage = BigDecimal(sizeCurrent.toDouble() / sizeTotal * 100).setScale(2, RoundingMode.HALF_UP)
			logger.info { "$sizeCurrent / $sizeTotal :: $percentage%" }
			true
		}
	}

	protected var temporaryFolder = TemporaryFolder()
		private set

	override fun beforeTest(description: Description) {
		temporaryFolder.create()
	}

	override fun afterTest(description: Description, result: TestResult) {
		temporaryFolder.delete()
	}

	protected fun getFullVersion(jvmHomePath: Path): String {
		val jvmBinDir = jvmHomePath.resolve("bin").toFile()
		logger.info { "jvmBinDir: $jvmBinDir" }
		jvmBinDir.exists() shouldBe true

		val process = ProcessBuilder(File(jvmBinDir, "java").absolutePath, "-fullversion").start()
		process.waitFor(5, TimeUnit.SECONDS)
		return process.errorStream.readBytes().toString(Charset.defaultCharset()).trim()
	}

	protected fun prepareTest(vendor: String, version: String) {
		logger.info { "temporaryFolder: ${temporaryFolder.root}" }
		logger.info { "vendor: $vendor, version: $version" }

		temporaryFolder.root.listFiles().forEach {
			it.deleteRecursively()
		}

		val propertiesFile = temporaryFolder.newFile("jvmw.properties").apply {
			mapOf(
				JvmWrapperPropertyKeys.JVMW_DEBUG to true,
				JvmWrapperPropertyKeys.JVM_VERSION to version,
				JvmWrapperPropertyKeys.JVM_VENDOR to vendor
			).forEach { key, value ->
				appendText("${key.name}=$value\n")
			}
		}
		logger.info { "--- properties file :: begin ---" }
		logger.info { propertiesFile.readText() }
		logger.info { "--- properties file :: end ---" }
		val workingDir = propertiesFile.parentFile
		workingDir.absolutePath shouldBe temporaryFolder.root.absolutePath

		File(System.getProperty("user.dir")).parentFile.resolve(SCRIPT_FILE_NAME).copyTo(File(workingDir, SCRIPT_FILE_NAME))
	}

	protected fun buildPreviousVersion(jvmVersionSample: JvmVersionRow): Jvm {
		val version = jvmVersionSample.version
		val vendor = jvmVersionSample.vendor
		val type = jvmVersionSample.type

		prepareTest(vendor, version)

		logger.info { "version: $version" }
		val jvm = Jvm(vendor = vendor.toJvmVendor(), type = type.toJvmType(), version = version)
		logger.info { "jvm: $jvm" }
		val downloader = jvm.downloader()

		val jvmHomePath = File(temporaryFolder.root, jvm.toString())

		val tempFile = downloader.downloadToTempFile(
			remoteArchiveFile = jvmVersionSample.remoteFiles.getValue(jvm.os), archiveFileExtension = jvm.archiveFileExtension
		)
		UnarchiverFactory.getInstance(sourceFile = tempFile, targetDir = jvmHomePath).unpack()

		val actualFullVersion = getFullVersion(jvmHomePath = jvmHomePath.toPath().fixFromMac())
		actualFullVersion shouldBe containIgnoringCase(""" full version "${jvmVersionSample.fullVersion}"""")

		return jvm
	}

	internal fun AbstractDownloader.downloadToTempFile(
		remoteArchiveFile: RemoteArchiveFile,
		downloadProcessListener: DownloadProcessListener? = this@AbstractIntegrationTests.downloadProcessListener,
		archiveFileExtension: String
	): File {
		val tempFile = File.createTempFile("tmp", ".$archiveFileExtension")
		tempFile.deleteOnExit()
		download(remoteArchiveFile = remoteArchiveFile, target = tempFile, downloadProcessListener = downloadProcessListener)
		return tempFile
	}

	companion object {
		val launchedInCI = System.getenv().containsKey("CI")
		val isNixOS = SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC
	}
}
