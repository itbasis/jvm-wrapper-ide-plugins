package ru.itbasis.jvmwrapper.core.unarchiver

import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import ru.itbasis.jvmwrapper.core.ProcessStepListener
import java.io.File

class LinuxUnarchiver(sourceFile: File, targetDir: File, stepListener: ProcessStepListener? = null, removeOriginal: Boolean = false) :
  AbstractUnarchiver(sourceFile, targetDir, stepListener, removeOriginal) {
  override fun doMovingToDest() {
    tempDir.listFiles().first().renameTo(targetDir)
  }

  override fun doUnpack() {
    TarArchiveInputStream(GzipCompressorInputStream(sourceFile.inputStream().buffered())).use { ais ->
      var entry = ais.nextEntry
      while (entry != null) {
        val destPath = File(tempDir, entry.name)

        if (entry.isDirectory) {
          destPath.mkdirs()
        } else {
          destPath.createNewFile()
          ais.copyTo(destPath.outputStream().buffered())
        }
        entry = ais.nextEntry
      }
    }
  }
}
