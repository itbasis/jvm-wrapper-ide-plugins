package ru.itbasis.jvmwrapper.core.unarchiver

import asRows
import io.kotlintest.data.forall
import io.kotlintest.matchers.file.shouldBeNonEmptyDirectory
import io.kotlintest.matchers.file.shouldNotBeEmpty
import io.kotlintest.matchers.file.shouldNotBeNonEmptyDirectory
import io.kotlintest.shouldBe
import mu.KotlinLogging
import org.apache.commons.lang3.SystemUtils.IS_OS_LINUX
import org.apache.commons.lang3.SystemUtils.IS_OS_MAC
import ru.itbasis.jvmwrapper.core.AbstractIntegrationTests
import ru.itbasis.jvmwrapper.core.downloader.downloader
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.jvmwrapper.core.jvm.toJvmType
import ru.itbasis.jvmwrapper.core.jvm.toJvmVendor
import samples.OpenJDKJvmVersionEarlyAccessSamples
import samples.OpenJDKJvmVersionLatestSamples
import samples.OracleJvmVersionArchiveSamples
import samples.OracleJvmVersionLatestSamples
import java.io.File

internal class UnarchiverIntegrationTest : AbstractIntegrationTests() {
	override val logger = KotlinLogging.logger {}

	private val rows = listOf(
		OpenJDKJvmVersionLatestSamples.firstOrNull(),
		OracleJvmVersionLatestSamples.firstOrNull(),
		OracleJvmVersionArchiveSamples.firstOrNull(),
		OpenJDKJvmVersionEarlyAccessSamples.firstOrNull()

	).asRows()

	init {
		test("unpack").config(enabled = IS_OS_LINUX || IS_OS_MAC) {
			forall(
				rows = *rows
			) { (vendor, type, version, _, _, _, _, _, _) ->
				val downloader = Jvm(
					vendor = vendor.toJvmVendor(), type = type.toJvmType(), version = version
				).downloader()
				val tempFile = File.createTempFile("tmp", "tmp")
				downloader.download(target = tempFile, downloadProcessListener = downloadProcessListener)
				tempFile.shouldNotBeEmpty()

				val targetDir = File(tempFile.parentFile, "unpack")
				targetDir.mkdirs()
				targetDir.shouldNotBeNonEmptyDirectory()

				UnarchiverFactory.getInstance(sourceFile = tempFile, targetDir = targetDir).unpack()
				targetDir.shouldBeNonEmptyDirectory()

				val actualJvm = Jvm(path = targetDir.toPath())
				actualJvm.version shouldBe version

				targetDir.deleteRecursively()
				tempFile.delete()
			}
		}
	}
}
