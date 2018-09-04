package ru.itbasis.jvmwrapper.core

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec
import io.kotlintest.tables.row
import ru.itbasis.jvmwrapper.core.JvmType.JDK
import ru.itbasis.jvmwrapper.core.JvmType.JVM

internal class JvmTypeTest : FunSpec({
  test("toJvmType") {
    forall(
      row("jdk", JDK), row("jvm", JVM), row("JDK", JDK), row("JVM", JVM), row("jDk", JDK)
    ) { value, expected ->
      value.toJvmType() shouldBe expected
    }
  }
})