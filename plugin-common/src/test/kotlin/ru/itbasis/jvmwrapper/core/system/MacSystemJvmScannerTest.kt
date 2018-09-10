package ru.itbasis.jvmwrapper.core.system

import io.kotlintest.matchers.collections.shouldContainAll
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.specs.FunSpec
import org.apache.commons.lang3.SystemUtils.IS_OS_MAC
import java.nio.file.Paths

internal class MacSystemJvmScannerTest : FunSpec() {
  init {

    test("get instance").config(enabled = IS_OS_MAC) {
      SystemJvmScanner.getInstance().shouldBeInstanceOf<MacSystemJvmScanner>()
    }

    test("list jvm paths").config(enabled = IS_OS_MAC) {
      val actualPaths = SystemJvmScanner.getInstance().listJvmPaths()

      actualPaths.shouldContainAll(arrayOf("1.6.0", "jdk1.8.0_181", "jdk-10.0.2", "jdk-11").map {
        Paths.get("/Library/Java/JavaVirtualMachines/$it.jdk/Contents/Home")
      })
    }

    test(name = "list jvm").config(enabled = IS_OS_MAC) {
      val actualJvmList = SystemJvmScanner.getInstance().listJvm()

      actualJvmList.map { it.toString() }.shouldContainAll("oracle-jdk-6u65", "oracle-jdk-8u181", "oracle-jdk-10.0.2", "oracle-jdk-11")
    }
  }
}