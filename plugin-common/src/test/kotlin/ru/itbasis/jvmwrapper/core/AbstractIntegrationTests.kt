package ru.itbasis.jvmwrapper.core

import io.kotlintest.specs.FunSpec

abstract class AbstractIntegrationTests : FunSpec() {
  companion object {
    fun launchedInCI() = System.getenv().containsKey("CI")
  }
}
