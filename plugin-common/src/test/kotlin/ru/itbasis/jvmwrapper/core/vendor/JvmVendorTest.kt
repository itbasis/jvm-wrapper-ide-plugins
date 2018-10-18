package ru.itbasis.jvmwrapper.core.vendor

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.FunSpec
import io.kotlintest.tables.row
import ru.itbasis.jvmwrapper.core.jvm.JvmVendor.ORACLE
import ru.itbasis.jvmwrapper.core.jvm.toJvmVendor
import samples.JvmVersionSamples
import samples.asKotlinTestRows

internal class JvmVendorTest : FunSpec({
	test("Successful parsing of vendors") {
		forall(
			row("oracle", ORACLE), row("Oracle", ORACLE), row("Oracle Corporation", ORACLE)
		) { value, expected ->
			value.toJvmVendor() shouldBe expected
		}
	}

	test("Successful parsing of vendors - 2") {
		forall(
			rows = *JvmVersionSamples.asKotlinTestRows()
		) { (vendor) ->
			vendor.toJvmVendor().code shouldBe vendor
		}
	}

	test("Unsuccessful parsing of vendors") {
		forall(
			row("oracle "), row("o")
		) { value ->
			shouldThrow<IllegalArgumentException> {
				value.toJvmVendor()
			}
		}
	}
})
