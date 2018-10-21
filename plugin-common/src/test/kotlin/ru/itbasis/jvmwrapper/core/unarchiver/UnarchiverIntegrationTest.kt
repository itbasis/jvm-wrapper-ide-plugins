@file:Suppress("Destructure")

package ru.itbasis.jvmwrapper.core.unarchiver

import io.kotlintest.data.forall
import io.kotlintest.matchers.file.shouldBeNonEmptyDirectory
import io.kotlintest.matchers.file.shouldNotBeEmpty
import io.kotlintest.matchers.file.shouldNotBeNonEmptyDirectory
import io.kotlintest.shouldBe
import mu.KotlinLogging
import ru.itbasis.jvmwrapper.core.AbstractIntegrationTests
import ru.itbasis.jvmwrapper.core.downloader.downloader
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.jvmwrapper.core.jvm.toJvmType
import ru.itbasis.jvmwrapper.core.jvm.toJvmVendor

internal class UnarchiverIntegrationTest : AbstractIntegrationTests() {
	override val logger = KotlinLogging.logger {}

	private fun testUnpack(vendor: String, type: String, version: String, expectedVersion: String) {
		val jvm = Jvm(vendor = vendor.toJvmVendor(), type = type.toJvmType(), version = version)
		logger.info { "jvm: $jvm" }
		val downloader = jvm.downloader()

		val tempFile = temporaryFolder.newFile("tmp_$jvm.${jvm.archiveFileExtension}")
		downloader.download(target = tempFile, downloadProcessListener = downloadProcessListener)
		tempFile.shouldNotBeEmpty()

		val targetDir = temporaryFolder.newFolder()
		targetDir.shouldNotBeNonEmptyDirectory()

		UnarchiverFactory.getInstance(sourceFile = tempFile, targetDir = targetDir).unpack()
		targetDir.shouldBeNonEmptyDirectory()

		val actualJvm = Jvm(path = targetDir.toPath())
		actualJvm.version shouldBe expectedVersion
	}

	init {
		test("unpack (version)").config(enabled = isNixOS) {
			forall(
				rows = *jvmFirstRows
			) { jvmVersionRow ->
				logger.info { "version: ${jvmVersionRow.version}" }

				testUnpack(jvmVersionRow.vendor, jvmVersionRow.type, jvmVersionRow.version, jvmVersionRow.cleanVersion)
			}
		}

		test("unpack (fullVersion)").config(enabled = isNixOS) {
			forall(
				rows = *jvmFirstRows
			) { jvmVersionRow ->
				logger.info { "version: ${jvmVersionRow.version}" }

				testUnpack(jvmVersionRow.vendor, jvmVersionRow.type, jvmVersionRow.fullVersion, jvmVersionRow.cleanVersion)
			}
		}
	}
}
