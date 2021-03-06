@file:Suppress("Destructure")

package ru.itbasis.jvmwrapper.core.wrapper

import io.kotlintest.data.forall
import io.kotlintest.matchers.file.startWithPath
import io.kotlintest.matchers.startWith
import io.kotlintest.matchers.string.containIgnoringCase
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldNot
import io.kotlintest.tables.row
import mu.KotlinLogging
import ru.itbasis.jvmwrapper.core.AbstractIntegrationTests
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.jvmwrapper.core.jvm.toJvmType
import ru.itbasis.jvmwrapper.core.jvm.toJvmVendor
import samples.JvmVersionRow
import samples.OpenJDKJvmVersionLatestSamples
import samples.asJvmVersionRow
import samples.jvmVersionSample__openjdk_jdk_11
import samples.jvmVersionSample__openjdk_jdk_11_0_1

internal class JvmWrapperTest : AbstractIntegrationTests() {
	override val logger = KotlinLogging.logger {}

	override fun isInstancePerTest(): Boolean {
		return false
	}

	private fun testJvmVersion(jvmVersionRow: JvmVersionRow): JvmWrapper {
		logger.info("jvmVersionRow=$jvmVersionRow")

		prepareTest(jvmVersionRow.vendor, jvmVersionRow.version)

		val jvmVendor = jvmVersionRow.vendor.toJvmVendor()
		val jvmType = jvmVersionRow.type.toJvmType()
		val jvm = Jvm(vendor = jvmVendor, type = jvmType, version = jvmVersionRow.version)
		val jvmWrapper = JvmWrapper(
			jvmwHomeDir = temporaryJvmWrapperFolder(),
			workingDir = temporaryFolder.root,
			stepListener = stepListener,
			downloadProcessListener = downloadProcessListener
		)
		val jvmHomeDir = jvmWrapper.jvmHomeDir
		logger.info { "jvmHomeDir: $jvmHomeDir" }
		jvmHomeDir should startWithPath(temporaryJvmWrapperFolder().resolve(jvm.toString()))

		val actualJvm = Jvm(path = jvmHomeDir.toPath())
// TODO https://github.com/itbasis/jvm-wrapper-ide-plugins/issues/9
//  actualJvm.vendor shouldBe jvmVendor
		actualJvm.type shouldBe jvmType
		actualJvm.major shouldBe jvmVersionRow.versionMajor
		actualJvm.update shouldBe jvmVersionRow.versionUpdate
		actualJvm.earlyAccess shouldBe jvmVersionRow.versionEarlyAccess

		val actualFullVersion = getFullVersion(jvmHomePath = jvmHomeDir.toPath())
		actualFullVersion shouldBe containIgnoringCase(""" full version "${jvmVersionRow.fullVersion}"""")

		return jvmWrapper
	}

	init {
		test("update jvm").config(enabled = false) {
			forall(
				row(jvmVersionSample__openjdk_jdk_11.asJvmVersionRow().first(), jvmVersionSample__openjdk_jdk_11_0_1.asJvmVersionRow().first())
			) { previousJvmVersionSample, jvmVersionSample ->
				val previousJvm = downloadVersion(previousJvmVersionSample)
				LastUpdateFile(jvm = previousJvm, jvmwHomeDir = temporaryJvmWrapperFolder()).file.delete()

				testJvmVersion(jvmVersionRow = jvmVersionSample)
			}
		}

		test("JVM reload if unpacked directory was deleted").config(enabled = false) {
			forall(
				row(OpenJDKJvmVersionLatestSamples.first().asJvmVersionRow().first())
			) { jvmVersionSample ->
				val jvmWrapper = testJvmVersion(jvmVersionRow = jvmVersionSample)

				val jvmHomeDir = jvmWrapper.jvmHomeDir
				jvmHomeDir.deleteRecursively()

				testJvmVersion(jvmVersionRow = jvmVersionSample)
				val actualFullVersion = getFullVersion(jvmHomePath = jvmHomeDir.toPath())
				actualFullVersion shouldBe containIgnoringCase(""" full version "${jvmVersionSample.fullVersion}"""")
			}
		}

		test("JvmWrapper Recall").config(enabled = false) {
			forall(
				row(OpenJDKJvmVersionLatestSamples.first().asJvmVersionRow().first())
			) { jvmVersionSample ->
				testJvmVersion(jvmVersionRow = jvmVersionSample)

				JvmWrapper(jvmwHomeDir = temporaryJvmWrapperFolder(), workingDir = temporaryFolder.root, stepListener = { msg ->
					logger.info { msg }
					msg shouldNot startWith("download remote archive:")
				}, downloadProcessListener = { _, _, _ ->
					throw IllegalStateException("")
				})
			}
		}

		test("test all versions").config(enabled = isNixOS) {
			forall(
				rows = *jvmAllRows
			) { jvmVersionRow ->
				testJvmVersion(jvmVersionRow = jvmVersionRow)
			}
		}
	}
}
