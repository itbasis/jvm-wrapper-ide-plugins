@file:Suppress("Destructure")

package ru.itbasis.jvmwrapper.core.jvm

import io.kotlintest.data.forall
import io.kotlintest.matchers.string.beUpperCase
import io.kotlintest.should
import io.kotlintest.shouldBe
import ru.itbasis.jvmwrapper.core.AbstractTests

internal class JvmTest : AbstractTests() {
	init {
		test("version") {
			forall(
				rows = *jvmAllRows
			) { jvmVersionRow ->
				val actual =
					Jvm(vendor = jvmVersionRow.vendor.toJvmVendor(), type = jvmVersionRow.type.toJvmType(), version = jvmVersionRow.version)

				actual.type.name should beUpperCase()
				actual.type.name.toLowerCase() shouldBe jvmVersionRow.type
				actual.major shouldBe jvmVersionRow.versionMajor
			}
		}

		test("full version") {
			forall(
				rows = *jvmAllRows
			) { jvmVersionRow ->
				val actual =
					Jvm(vendor = jvmVersionRow.vendor.toJvmVendor(), type = jvmVersionRow.type.toJvmType(), version = jvmVersionRow.fullVersion)

				actual.type.name should beUpperCase()
				actual.type.name.toLowerCase() shouldBe jvmVersionRow.type
				actual.major shouldBe jvmVersionRow.versionMajor
				actual.update shouldBe jvmVersionRow.versionUpdate
				actual.earlyAccess shouldBe jvmVersionRow.versionEarlyAccess
				actual.cleanVersion shouldBe jvmVersionRow.cleanVersion
			}
		}
	}
}