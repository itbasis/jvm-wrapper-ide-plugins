@file:Suppress("Destructure")

package ru.itbasis.jvmwrapper.core.wrapper

import io.kotlintest.data.forall
import io.kotlintest.matchers.file.startWithPath
import io.kotlintest.matchers.string.containIgnoringCase
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.tables.row
import mu.KotlinLogging
import ru.itbasis.jvmwrapper.core.AbstractIntegrationTests
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.jvmwrapper.core.jvm.toJvmType
import ru.itbasis.jvmwrapper.core.jvm.toJvmVendor
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

	private fun testJvmVersion(jvmVersionRow: JvmVersionRow) {
		logger.info("jvmVersionRow=$jvmVersionRow")

		prepareTest(jvmVersionRow.vendor, jvmVersionRow.version)

		val jvm = Jvm(
			vendor = jvmVersionRow.vendor.toJvmVendor(), type = jvmVersionRow.type.toJvmType(), version = jvmVersionRow.version
		)
		val jvmWrapper = JvmWrapper(
			workingDir = temporaryFolder.root, stepListener = stepListener, downloadProcessListener = downloadProcessListener
		)
		val jvmHomeDir = jvmWrapper.jvmHomeDir
		logger.info { "jvmHomeDir: $jvmHomeDir" }
		jvmHomeDir should startWithPath(temporaryFolder.root.resolve(jvm.toString()))

		val actualFullVersion = getFullVersion(jvmHomePath = jvmHomeDir.toPath())
		actualFullVersion shouldBe containIgnoringCase(""" full version "${jvmVersionRow.fullVersion}"""")
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

				testJvmVersion(jvmVersionRow = jvmVersionSample)
			}
		}

		test("test all versions").config(enabled = isNixOS) {
			//		test("test all versions").config(enabled = false) {
			forall(
				rows = *jvmAllRows
			) { jvmVersionRow ->
				testJvmVersion(jvmVersionRow = jvmVersionRow)
			}
		}
	}
}
