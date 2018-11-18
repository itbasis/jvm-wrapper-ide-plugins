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
import ru.itbasis.jvmwrapper.core.OsType
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.jvmwrapper.core.jvm.toJvmType
import ru.itbasis.jvmwrapper.core.jvm.toJvmVendor
import samples.asKotlinTestRows
import samples.jvmVersionSample__openjdk_jdk_11_0_1

internal class DownloaderIntegrationTest : AbstractIntegrationTests() {
	override val logger = KotlinLogging.logger {}

	init {
		test("resolve urls").config(enabled = true) {
			forall(
				rows = *jvmAllRows
//				rows = *listOf(jvmVersionSample__openjdk_jdk_11_0_1).asKotlinTestRows()
			) { jvmVersionRow ->
				logger.info { "version: ${jvmVersionRow.version}" }
				val jvm = Jvm(vendor = jvmVersionRow.vendor.toJvmVendor(), type = jvmVersionRow.type.toJvmType(), version = jvmVersionRow.version)
				logger.info { "jvm: $jvm" }
				val downloader = jvm.downloader()
				logger.info { "downloader: $downloader" }

				val remoteArchiveFile = downloader.remoteArchiveFile
				logger.info { "remoteArchiveFile.url = ${remoteArchiveFile.url}" }
				val expectedUrl = jvmVersionRow.remoteFiles[OsType.currentOs()]!!.url
				logger.info { "expectedUrl: $expectedUrl" }

				remoteArchiveFile.url shouldBe expectedUrl
			}
		}

		test("resolve and download use downloadProcessListener").config(enabled = true) {
			forall(
				rows = *jvmFirstRows
			) { jvmVersionRow ->
				logger.info { "version: ${jvmVersionRow.version}" }
				val jvm = Jvm(vendor = jvmVersionRow.vendor.toJvmVendor(), type = jvmVersionRow.type.toJvmType(), version = jvmVersionRow.version)
				logger.info { "jvm: $jvm" }
				val downloader = jvm.downloader()

				val remoteArchiveFile = downloader.remoteArchiveFile
				remoteArchiveFile shouldNotBe null
				remoteArchiveFile.url should startWith(jvmVersionRow.downloadArchiveUrlPart)
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
			) { jvmVersionRow ->
				logger.info { "version: ${jvmVersionRow.version}" }
				val jvm = Jvm(vendor = jvmVersionRow.vendor.toJvmVendor(), type = jvmVersionRow.type.toJvmType(), version = jvmVersionRow.version)
				logger.info { "jvm: $jvm" }
				val downloader = jvm.downloader()

				val remoteArchiveFile = downloader.remoteArchiveFile
				remoteArchiveFile shouldNotBe null
				remoteArchiveFile.url should startWith(jvmVersionRow.downloadArchiveUrlPart)
				logger.info { "remoteArchiveFile.url = ${remoteArchiveFile.url}" }

				val tempFile = temporaryFolder.newFile()
				downloader.download(target = tempFile)
				tempFile.shouldNotBeEmpty()
				tempFile.length() shouldBe beGreaterThan(10 * 1024)
			}
		}
	}
}