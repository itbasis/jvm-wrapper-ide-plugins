package ru.itbasis.jvmwrapper.core.wrapper

import asRows
import io.kotlintest.Description
import io.kotlintest.TestResult
import io.kotlintest.data.forall
import io.kotlintest.matchers.file.startWithPath
import io.kotlintest.should
import io.kotlintest.shouldBe
import mu.KotlinLogging
import org.junit.rules.TemporaryFolder
import ru.itbasis.jvmwrapper.core.AbstractIntegrationTests
import ru.itbasis.jvmwrapper.core.JvmVersionArchiveSamples
import ru.itbasis.jvmwrapper.core.JvmVersionLatestSamples
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

internal class JvmWrapperTest : AbstractIntegrationTests() {
  private val logger = KotlinLogging.logger {}

  private val stepListener: (String) -> Unit = { msg -> logger.info { msg } }
  private val downloadProcessListener: (String, Long, Long) -> Boolean = { _, sizeCurrent, sizeTotal ->
    val percentage = BigDecimal(sizeCurrent.toDouble() / sizeTotal * 100).setScale(2, RoundingMode.HALF_UP)
    logger.info { "$sizeCurrent / $sizeTotal :: $percentage%" }
    true
  }
  private var temporaryFolder = TemporaryFolder()

  override fun beforeTest(description: Description) {
    temporaryFolder.create()
  }

  override fun afterTest(description: Description, result: TestResult) {
    temporaryFolder.delete()
  }

  private fun prepareTest(vendor: String, version: String) {
    logger.info { "temporaryFolder: ${temporaryFolder.root}" }
    logger.info { "version: $version" }
    temporaryFolder.root.listFiles().forEach { it.deleteRecursively() }

    val propertiesFile = temporaryFolder.newFile("jvmw.properties").apply {
      appendText("JVM_VERSION=$version\n")
      appendText("JVM_VENDOR=$vendor")
    }
    val workingDir = propertiesFile.parentFile
    workingDir.absolutePath shouldBe temporaryFolder.root.absolutePath

    File(System.getProperty("user.dir")).parentFile.resolve(SCRIPT_FILE_NAME).copyTo(File(workingDir, SCRIPT_FILE_NAME))
  }

  init {
    test("test latest versions") {
      forall(
        rows = *JvmVersionLatestSamples.plus(JvmVersionArchiveSamples).asRows()
//        rows = *JvmVersionArchiveSamples.asRows()
//        rows = *JvmVersionLatestSamples.asRows()
      ) { (vendor, type, version, fullVersion, cleanVersion, _, _, _, _) ->
        prepareTest(vendor, version)

        val jvmWrapper = JvmWrapper(
          workingDir = temporaryFolder.root,
          stepListener = stepListener,
          downloadProcessListener = if (!launchedInCI()) downloadProcessListener else null
        )
        val jvmHomeDir = jvmWrapper.jvmHomeDir
        logger.info { "jvmHomeDir: $jvmHomeDir" }
        jvmHomeDir should startWithPath(JVMW_HOME_DIR.resolve("$vendor-$type-$cleanVersion"))

        val jvmBinDir = jvmHomeDir.resolve("bin")
        logger.info { "jvmBinDir: $jvmBinDir" }
        jvmBinDir.exists() shouldBe true

        val process = ProcessBuilder(File(jvmBinDir, "java").absolutePath, "-fullversion").start()
        process.waitFor(5, TimeUnit.SECONDS)
        """java full version "$fullVersion"""" shouldBe process.errorStream.readBytes().toString(Charset.defaultCharset()).trim()
      }
    }
  }
}
