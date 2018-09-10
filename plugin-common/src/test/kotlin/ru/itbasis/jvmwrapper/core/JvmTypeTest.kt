package ru.itbasis.jvmwrapper.core

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec
import io.kotlintest.tables.row
import ru.itbasis.jvmwrapper.core.jvm.JvmType.JDK
import ru.itbasis.jvmwrapper.core.jvm.JvmType.JRE
import ru.itbasis.jvmwrapper.core.jvm.toJvmType

internal class JvmTypeTest : FunSpec({
  test("toJvmType") {
    forall(
      row("jdk", JDK), row("jre", JRE), row("JDK", JDK), row("JRE", JRE), row("jDk", JDK)
    ) { value, expected ->
      value.toJvmType() shouldBe expected
    }
  }
})