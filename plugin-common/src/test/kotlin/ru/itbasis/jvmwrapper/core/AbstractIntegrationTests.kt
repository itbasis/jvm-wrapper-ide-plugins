package ru.itbasis.jvmwrapper.core

import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec
import mu.KLogger
import org.apache.commons.lang3.SystemUtils
import ru.itbasis.jvmwrapper.core.downloader.AbstractDownloader
import ru.itbasis.jvmwrapper.core.downloader.DownloadProcessListener
import ru.itbasis.jvmwrapper.core.downloader.RemoteArchiveFile
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

	protected fun getFullVersion(jvmHomePath: Path): String {
		val jvmBinDir = jvmHomePath.resolve("bin").toFile()
		logger.info { "jvmBinDir: $jvmBinDir" }
		jvmBinDir.exists() shouldBe true

		val process = ProcessBuilder(File(jvmBinDir, "java").absolutePath, "-fullversion").start()
		process.waitFor(5, TimeUnit.SECONDS)
		return process.errorStream.readBytes().toString(Charset.defaultCharset()).trim()
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
