package ru.itbasis.jvmwrapper.core.jvm

import asRows
import io.kotlintest.data.forall
import io.kotlintest.matchers.string.beUpperCase
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec
import samples.JvmVersionSamples

internal class JvmTest : FunSpec() {
	init {
		test("version") {
			forall(
				rows = *JvmVersionSamples.asRows()
			) { (vendor, type, version, _, cleanVersion, versionMajor, versionUpdate, _, _) ->
				val actual = Jvm(vendor = vendor.toJvmVendor(), type = type.toJvmType(), version = version)

				actual.type.name should beUpperCase()
				actual.type.name.toLowerCase() shouldBe type
				actual.major shouldBe versionMajor
				actual.update shouldBe versionUpdate
				actual.cleanVersion shouldBe cleanVersion
			}
		}
	}
}