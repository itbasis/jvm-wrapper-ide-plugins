package ru.itbasis.jvmwrapper.core.vendor

import asRows
import io.kotlintest.data.forall
import io.kotlintest.matchers.file.shouldNotBeEmpty
import io.kotlintest.matchers.startWith
import io.kotlintest.should
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.FunSpec
import mu.KotlinLogging
import ru.itbasis.jvmwrapper.core.JvmVersionArchiveSamples
import ru.itbasis.jvmwrapper.core.JvmVersionLatestSamples
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.jvmwrapper.core.jvm.toJvmType
import ru.itbasis.jvmwrapper.core.vendor.oracle.OracleProvider
import java.io.File

internal class OracleProviderIntegrationTest : FunSpec() {
  private val logger = KotlinLogging.logger {}

  init {
    test("resolve and download") {
      forall(
        rows = *JvmVersionLatestSamples.plus(JvmVersionArchiveSamples).asRows()
//      rows = *JvmVersionArchiveSamples.asRows()
      ) { (vendor, type, version, _, _, _, _, _, downloadArchiveUrlPart) ->
        logger.info { "version: $version" }
        val oracleProvider = OracleProvider(Jvm(vendor = vendor.toJvmVendor(), type = type.toJvmType(), version = version))

        val remoteArchiveFile = oracleProvider.remoteArchiveFile
        remoteArchiveFile shouldNotBe null
        remoteArchiveFile.url should startWith(downloadArchiveUrlPart)
        logger.info { "remoteArchiveFile.url = ${remoteArchiveFile.url}" }

        val tempFile = File.createTempFile("tmp", "tmp")
        oracleProvider.download(tempFile)
        tempFile.shouldNotBeEmpty()
        // FIXME tempFile.length() shouldBe beGreaterThan(10 * 1024)
      }
    }
  }
}