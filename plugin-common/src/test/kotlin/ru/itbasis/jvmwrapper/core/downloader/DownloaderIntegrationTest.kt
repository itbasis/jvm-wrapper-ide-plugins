package ru.itbasis.jvmwrapper.core.downloader

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

internal class DownloaderIntegrationTest : AbstractIntegrationTests() {
	override val logger = KotlinLogging.logger {}

	init {
		test("resolve and download use downloadProcessListener").config(enabled = true) {
			forall(
				rows = *jvmFirstRows
			) { (vendor, type, version, _, _, _, _, _, downloadArchiveUrlPart) ->
				logger.info { "version: $version" }
				val jvm = Jvm(vendor = vendor.toJvmVendor(), type = type.toJvmType(), version = version)
				logger.info { "jvm: $jvm" }
				val downloader = jvm.downloader()

				val remoteArchiveFile = downloader.remoteArchiveFile
				remoteArchiveFile shouldNotBe null
				remoteArchiveFile.url should startWith(downloadArchiveUrlPart)
				logger.info { "remoteArchiveFile.url = ${remoteArchiveFile.url}" }

				val tempFile = temporaryFolder.newFile()

				downloader.download(target = tempFile, downloadProcessListener = downloadProcessListener)
				tempFile.shouldNotBeEmpty()
				tempFile.length() shouldBe beGreaterThan(10 * 1024)

				val tempFile1 = downloader.downloadToTempFile(remoteArchiveFile = remoteArchiveFile)
				tempFile1.shouldNotBeEmpty()
				tempFile1.length() shouldBe beGreaterThan(10 * 1024)
				tempFile1.length() shouldBe tempFile.length()
				tempFile1 shouldNotBe tempFile
			}
		}

		test("resolve and download not use downloadProcessListener").config(enabled = true) {
			forall(
				rows = *jvmFirstRows
			) { (vendor, type, version, _, _, _, _, _, downloadArchiveUrlPart) ->
				logger.info { "version: $version" }
				val jvm = Jvm(vendor = vendor.toJvmVendor(), type = type.toJvmType(), version = version)
				logger.info { "jvm: $jvm" }
				val downloader = jvm.downloader()

				val remoteArchiveFile = downloader.remoteArchiveFile
				remoteArchiveFile shouldNotBe null
				remoteArchiveFile.url should startWith(downloadArchiveUrlPart)
				logger.info { "remoteArchiveFile.url = ${remoteArchiveFile.url}" }

				val tempFile = temporaryFolder.newFile()
				downloader.download(target = tempFile)
				tempFile.shouldNotBeEmpty()
				tempFile.length() shouldBe beGreaterThan(10 * 1024)
			}
		}
	}
}