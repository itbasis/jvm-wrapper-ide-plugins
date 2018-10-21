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
import java.io.File

internal class UnarchiverIntegrationTest : AbstractIntegrationTests() {
	override val logger = KotlinLogging.logger {}

	init {
		test("unpack").config(enabled = isNixOS) {
			forall(
				rows = *jvmFirstRows
			) { (vendor, type, version, fullVersion, _, _, _, _, _) ->
				logger.info { "version: $version" }
				val jvm = Jvm(vendor = vendor.toJvmVendor(), type = type.toJvmType(), version = version)
				logger.info { "jvm: $jvm" }
				val downloader = jvm.downloader()

				val tempFile = File.createTempFile("tmp", "." + jvm.archiveFileExtension)
				downloader.download(target = tempFile, downloadProcessListener = downloadProcessListener)
				tempFile.shouldNotBeEmpty()

				val targetDir = File(tempFile.parentFile, "unpack")
				targetDir.deleteRecursively()
				targetDir.mkdirs()
				targetDir.shouldNotBeNonEmptyDirectory()

				UnarchiverFactory.getInstance(sourceFile = tempFile, targetDir = targetDir).unpack()
				targetDir.shouldBeNonEmptyDirectory()

				val actualJvm = Jvm(path = targetDir.toPath())
				actualJvm.version shouldBe fullVersion

				tempFile.deleteRecursively()
				targetDir.deleteRecursively()
			}
		}
	}
}
