package ru.itbasis.jvmwrapper.core.vendor

import asRows
import io.kotlintest.data.forall
import io.kotlintest.matchers.beGreaterThan
import io.kotlintest.matchers.startWith
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.FunSpec
import ru.itbasis.jvmwrapper.core.JvmVersion
import ru.itbasis.jvmwrapper.core.JvmVersionArchiveSamples
import ru.itbasis.jvmwrapper.core.JvmVersionLatestSamples
import ru.itbasis.jvmwrapper.core.vendor.oracle.OracleProvider
import java.io.File

internal class OracleProviderIntegrationTest : FunSpec({
  test("resolve and download") {
    forall(
      rows = *JvmVersionLatestSamples.plus(JvmVersionArchiveSamples).asRows()
//      rows = *JvmVersionArchiveSamples.asRows()
    ) { (_, _, version, _, _, _, _, _, downloadArchiveUrlPart) ->
      println(version)
      val oracleProvider = OracleProvider(JvmVersion(version = version))

      val remoteArchiveFile = oracleProvider.remoteArchiveFile
      remoteArchiveFile shouldNotBe null
      remoteArchiveFile.url should startWith(downloadArchiveUrlPart)

      val tempFile = File.createTempFile("tmp", "tmp")
      oracleProvider.download(tempFile)
//      tempFile.isFile shouldBe true
//      tempFile.length() shouldBe beGreaterThan(10 * 1024)
    }
  }
})
