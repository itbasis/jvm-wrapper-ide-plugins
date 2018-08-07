package ru.itbasis.jvmwrapper.core.vendor

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.FunSpec
import io.kotlintest.tables.row
import ru.itbasis.jvmwrapper.core.vendor.JvmVendor.ORACLE

internal class JvmVendorTest : FunSpec({
  test("Successful parsing of vendors") {
    forall(
      row("oracle", ORACLE), row("Oracle", ORACLE), row("Oracle Corporation", ORACLE)
    ) { value, expected ->
      JvmVendor.parse(value) shouldBe expected
    }
  }

  test("runtime") {
    JvmVendor.runtime() shouldBe ORACLE
  }

  test("Unsuccessful parsing of vendors") {
    forall(row("oracle "), row("o")) { value ->
      shouldThrow<IllegalArgumentException> {
        JvmVendor.parse(value)
      }
    }
  }
})
