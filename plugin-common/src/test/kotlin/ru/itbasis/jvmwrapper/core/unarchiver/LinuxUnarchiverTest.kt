package ru.itbasis.jvmwrapper.core.unarchiver

import io.kotlintest.specs.FunSpec
import org.apache.commons.lang3.SystemUtils.IS_OS_LINUX
import java.io.File

internal class LinuxUnarchiverTest : FunSpec({
  test("unpack").config(enabled = IS_OS_LINUX) {
    LinuxUnarchiver(
      File("/Users/victor.alenkov/Downloads/jdk/jdk-8u171-linux-x64.tar.gz"), File("/Users/victor.alenkov/Downloads/jdk/jdk-8u171")
    ).unpack()
  }
})
