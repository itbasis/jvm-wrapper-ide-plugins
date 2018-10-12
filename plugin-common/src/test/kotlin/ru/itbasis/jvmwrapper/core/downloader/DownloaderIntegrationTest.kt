package ru.itbasis.jvmwrapper.core.downloader

import asRows
import io.kotlintest.data.forall
import io.kotlintest.matchers.beGreaterThan
import io.kotlintest.matchers.file.shouldNotBeEmpty
import io.kotlintest.matchers.startWith
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import mu.KotlinLogging
import ru.itbasis.jvmwrapper.core.AbstractIntegrationTests
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.jvmwrapper.core.jvm.toJvmType
import ru.itbasis.jvmwrapper.core.jvm.toJvmVendor
import samples.OpenJDKJvmVersionEarlyAccessSamples
import samples.OpenJDKJvmVersionLatestSamples
import samples.OracleJvmVersionArchiveSamples
import samples.OracleJvmVersionLatestSamples
import java.io.File

internal class DownloaderIntegrationTest : AbstractIntegrationTests() {
	override val logger = KotlinLogging.logger {}

	private val rows = listOf(
		OpenJDKJvmVersionLatestSamples.firstOrNull(),
		OracleJvmVersionLatestSamples.firstOrNull(),
		OracleJvmVersionArchiveSamples.firstOrNull(),
		OpenJDKJvmVersionEarlyAccessSamples.firstOrNull()

	).asRows()

	init {
		test("resolve and download use downloadProcessListener").config(enabled = true) {
			forall(
				rows = *rows
			) { (vendor, type, version, _, _, _, _, _, downloadArchiveUrlPart) ->
				logger.info { "version: $version" }
				val downloader = Jvm(
					vendor = vendor.toJvmVendor(), type = type.toJvmType(), version = version
				).downloader()

				val remoteArchiveFile = downloader.remoteArchiveFile
				remoteArchiveFile shouldNotBe null
				remoteArchiveFile.url should startWith(downloadArchiveUrlPart)
				logger.info { "remoteArchiveFile.url = ${remoteArchiveFile.url}" }

				val tempFile = File.createTempFile("tmp", "tmp")
				downloader.download(target = tempFile, downloadProcessListener = downloadProcessListener)
				tempFile.shouldNotBeEmpty()
				tempFile.length() shouldBe beGreaterThan(10 * 1024)

				tempFile.delete()
			}
		}

		test("resolve and download not use downloadProcessListener").config(enabled = true) {
			forall(
				rows = *rows
			) { (vendor, type, version, _, _, _, _, _, downloadArchiveUrlPart) ->
				logger.info { "version: $version" }
				val downloader = Jvm(
					vendor = vendor.toJvmVendor(), type = type.toJvmType(), version = version
				).downloader()

				val remoteArchiveFile = downloader.remoteArchiveFile
				remoteArchiveFile shouldNotBe null
				remoteArchiveFile.url should startWith(downloadArchiveUrlPart)
				logger.info { "remoteArchiveFile.url = ${remoteArchiveFile.url}" }

				val tempFile = File.createTempFile("tmp", "tmp")
				downloader.download(target = tempFile)
				tempFile.shouldNotBeEmpty()
				tempFile.length() shouldBe beGreaterThan(10 * 1024)

				tempFile.delete()
			}
		}
	}
}