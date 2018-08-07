package ru.itbasis.jvmwrapper.core

import asRows
import io.kotlintest.Description
import io.kotlintest.TestResult
import io.kotlintest.data.forall
import io.kotlintest.matchers.file.startWithPath
import io.kotlintest.should
import io.kotlintest.shouldBe
import org.junit.rules.TemporaryFolder
import ru.itbasis.jvmwrapper.core.JvmWrapper.Companion.JVMW_HOME_DIR
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

internal class JvmWrapperTest : AbstractIntegrationTests() {
  private val stepListener: (String) -> Unit = { msg -> println(msg) }
  private val downloadProcessListener: (String, Long, Long) -> Boolean = { _, sizeCurrent, sizeTotal ->
    val percentage = BigDecimal(sizeCurrent.toDouble() / sizeTotal * 100).setScale(2, RoundingMode.HALF_UP)
    println("$sizeCurrent / $sizeTotal :: $percentage%")
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
    println("temporaryFolder: ${temporaryFolder.root}")
    println("version: $version")
    temporaryFolder.root.listFiles().forEach { it.deleteRecursively() }

    val propertiesFile = temporaryFolder.newFile("jvmw.properties").apply {
      appendText("JVM_VERSION=$version\n")
      appendText("JVM_VENDOR=$vendor")
    }
    val workingDir = propertiesFile.parentFile
    workingDir.absolutePath shouldBe temporaryFolder.root.absolutePath

    File(System.getProperty("user.dir")).parentFile.resolve(JvmWrapper.SCRIPT_FILE_NAME)
      .copyTo(File(workingDir, JvmWrapper.SCRIPT_FILE_NAME))
  }

  init {
    test("test latest versions") {
      forall(
        rows = *JvmVersionLatestSamples.asRows()
      ) { (vendor, type, version, fullVersion, cleanVersion, _, _, _, _) ->
        prepareTest(vendor, version)

        val jvmWrapper = JvmWrapper(
          workingDir = temporaryFolder.root,
          stepListener = stepListener,
          downloadProcessListener = if (!launchedInCI()) downloadProcessListener else null
        )
        val jvmHomeDir = jvmWrapper.jvmHomeDir
        println("jvmHomeDir: $jvmHomeDir")
        jvmHomeDir should startWithPath(JVMW_HOME_DIR.resolve("$vendor-$type-$cleanVersion"))

        val jvmBinDir = jvmHomeDir.resolve("bin")
        println("jvmBinDir: $jvmBinDir")
        jvmBinDir.exists() shouldBe true

        val process = ProcessBuilder(File(jvmBinDir, "java").absolutePath, "-fullversion").start()
        process.waitFor(5, TimeUnit.SECONDS)
        """java full version "$fullVersion"""" shouldBe process.errorStream.readBytes().toString(Charset.defaultCharset()).trim()
      }
    }
  }
}
