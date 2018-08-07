package ru.itbasis.jvmwrapper.core.vendor

import asRows
import io.kotlintest.data.forall
import io.kotlintest.matchers.string.beUpperCase
import io.kotlintest.should
import io.kotlintest.shouldBe
import ru.itbasis.jvmwrapper.core.AbstractIntegrationTests
import ru.itbasis.jvmwrapper.core.JvmVersion
import ru.itbasis.jvmwrapper.core.JvmVersionLatestSamples
import ru.itbasis.jvmwrapper.core.jvmVersionSample__oracle_jdk_8u171

class JvmVersionTest : AbstractIntegrationTests() {
	init {
		test("version") {
			forall(
				rows = *JvmVersionLatestSamples.asRows()
			) { (_, type, version, _, cleanVersion, versionMajor, versionUpdate, _, _) ->
				val actual = JvmVersion(version = version)

				actual.type.name should beUpperCase()
				actual.type.name.toLowerCase() shouldBe type
				actual.major shouldBe versionMajor
				actual.update shouldBe versionUpdate
				actual.cleanVersion shouldBe cleanVersion
			}
		}

		test("runtime version").config(enabled = launchedInCI()) {
			val expected = jvmVersionSample__oracle_jdk_8u171
			val actual = JvmVersion(version = System.getProperty("java.version"))

			actual.major shouldBe expected.versionMajor
			actual.update shouldBe expected.versionUpdate
			actual.cleanVersion shouldBe expected.cleanVersion
		}
	}
}
