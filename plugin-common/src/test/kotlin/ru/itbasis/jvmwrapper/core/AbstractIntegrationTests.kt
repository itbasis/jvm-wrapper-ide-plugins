package ru.itbasis.jvmwrapper.core

import io.kotlintest.Description
import io.kotlintest.TestResult
import io.kotlintest.TestStatus
import io.kotlintest.matchers.file.aDirectory
import io.kotlintest.matchers.file.startWithPath
import io.kotlintest.matchers.string.containIgnoringCase
import io.kotlintest.should
import io.kotlintest.shouldBe
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
import ru.itbasis.jvmwrapper.core.wrapper.JVMW_PROPERTY_FILE_NAME
import ru.itbasis.jvmwrapper.core.wrapper.JvmWrapperPropertyKeys
import samples.JvmVersionRow
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.charset.Charset
import java.nio.file.Path
import java.util.concurrent.TimeUnit

abstract class AbstractIntegrationTests : AbstractTests() {
	abstract val logger: KLogger

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

	protected fun temporaryJvmWrapperFolder(): File {
		val folderName = ".jvmw"
		return File(temporaryFolder.root, folderName).takeIf { it.isDirectory }
		       ?: temporaryFolder.newFolder(folderName)
	}

	override fun beforeTest(description: Description) {
		temporaryFolder.create()
	}

	override fun afterTest(description: Description, result: TestResult) {
		if (result.status == TestStatus.Success) {
			temporaryFolder.delete()
		}
	}

	protected fun getFullVersion(jvmHomePath: Path): String {
		val jvmBinDir = jvmHomePath.resolve("bin").toFile()
		logger.info { "jvmBinDir: $jvmBinDir" }
		jvmBinDir should aDirectory()

		val process = ProcessBuilder(File(jvmBinDir, "java").absolutePath, "-fullversion").start()
		process.waitFor(5, TimeUnit.SECONDS)
		return process.errorStream.readBytes().toString(Charset.defaultCharset()).trim()
	}

	protected fun prepareTest(vendor: String, version: String) {
		logger.info { "temporaryFolder: ${temporaryFolder.root}" }
		logger.info { "vendor: $vendor, version: $version" }

		File(temporaryFolder.root, JVMW_PROPERTY_FILE_NAME).takeIf { it.exists() }?.delete()
		val propertiesFile = temporaryFolder.newFile(JVMW_PROPERTY_FILE_NAME).apply {
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
	}

	protected fun downloadVersion(jvmVersionSample: JvmVersionRow): Jvm {
		val version = jvmVersionSample.version
		val vendor = jvmVersionSample.vendor
		val type = jvmVersionSample.type

		prepareTest(vendor, version)

		logger.info { "version: $version" }
		val jvm = Jvm(vendor = vendor.toJvmVendor(), type = type.toJvmType(), version = version)
		logger.info { "jvm: $jvm" }
		val downloader = jvm.downloader()

		val jvmHomePath = File(temporaryJvmWrapperFolder(), jvm.toString())

		val tempFile = downloader.downloadToTempFile(
			remoteArchiveFile = jvmVersionSample.remoteFiles.getValue(jvm.os)
		)
		UnarchiverFactory.getInstance(sourceFile = tempFile, targetDir = jvmHomePath).unpack()

		jvmHomePath should startWithPath(temporaryJvmWrapperFolder().resolve(jvm.toString()))
		val actualFullVersion = getFullVersion(jvmHomePath = jvmHomePath.toPath().fixFromMac())
		actualFullVersion shouldBe containIgnoringCase(""" full version "${jvmVersionSample.fullVersion}"""")

		return jvm
	}

	internal fun AbstractDownloader.downloadToTempFile(
		remoteArchiveFile: RemoteArchiveFile,
		downloadProcessListener: DownloadProcessListener? = this@AbstractIntegrationTests.downloadProcessListener
	): File {

		val tempFile = File.createTempFile("jvmw_", remoteArchiveFile.url.substringAfterLast("/"))
		tempFile.deleteOnExit()
		download(remoteArchiveFile = remoteArchiveFile, target = tempFile, downloadProcessListener = downloadProcessListener)
		return tempFile
	}

	companion object {
		val launchedInCI = System.getenv().containsKey("CI")
		val isNixOS = SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC
	}
}
