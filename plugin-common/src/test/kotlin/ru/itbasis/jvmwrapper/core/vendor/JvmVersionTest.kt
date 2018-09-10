package ru.itbasis.jvmwrapper.core.vendor

import asRows
import io.kotlintest.data.forall
import io.kotlintest.matchers.string.beUpperCase
import io.kotlintest.should
import io.kotlintest.shouldBe
import ru.itbasis.jvmwrapper.core.AbstractIntegrationTests
import ru.itbasis.jvmwrapper.core.JvmVersionLatestSamples
import ru.itbasis.jvmwrapper.core.jvm.Jvm

//class JvmVersionTest : AbstractIntegrationTests() {
//  init {
//    test("version") {
//      forall(
//        rows = *JvmVersionLatestSamples.asRows()
//      ) { (vendor, type, version, _, cleanVersion, versionMajor, versionUpdate, _, _) ->
//        val actual = Jvm(vendor = JvmVendor.parse(vendor), version = version)
//
//        actual.type.name should beUpperCase()
//        actual.type.name.toLowerCase() shouldBe type
//        actual.major shouldBe versionMajor
//        actual.update shouldBe versionUpdate
//        actual.cleanVersion shouldBe cleanVersion
//      }
//    }
//  }
//}
