package ru.itbasis.jvmwrapper.core.unarchiver

import org.apache.commons.lang3.SystemUtils
import ru.itbasis.jvmwrapper.core.ProcessStepListener
import ru.itbasis.jvmwrapper.core.archiveNameWithoutExtension
import ru.itbasis.jvmwrapper.core.step
import java.io.File
import java.util.concurrent.TimeUnit

abstract class AbstractUnarchiver(
  protected val sourceFile: File,
  protected val targetDir: File,
  protected val stepListener: ProcessStepListener? = null,
  private val removeOriginal: Boolean = false
) {
  protected val sourceFileName = sourceFile.archiveNameWithoutExtension()
  protected val tempDir = createTempDir(suffix = sourceFileName)

  fun unpack() {
    try {
      "Unpacking archive: $sourceFile".step(stepListener) { doUnpack() }
      "Moving unpacked content".step(stepListener) { doMovingToDest() }
    } finally {
      doFinalize()
    }
    if (removeOriginal) "Removing source archive: ".step(stepListener) { sourceFile.delete() }
  }

  protected abstract fun doUnpack()
  protected abstract fun doMovingToDest()
  protected open fun doFinalize() {}

  protected fun Process.run() {
    this.waitFor(10, TimeUnit.SECONDS)
//    process.inputStream.copyTo(System.out)
//    process.errorStream.copyTo(System.out)
  }
}

object UnarchiverFactory {
  fun getInstance(
    sourceFile: File, targetDir: File, stepListener: ProcessStepListener? = null, removeOriginal: Boolean = false
  ): AbstractUnarchiver {
    if (SystemUtils.IS_OS_MAC) {
      return MacUnarchiver(sourceFile, targetDir, stepListener, removeOriginal)
    }
    return LinuxUnarchiver(sourceFile, targetDir, stepListener, removeOriginal)
  }
}
