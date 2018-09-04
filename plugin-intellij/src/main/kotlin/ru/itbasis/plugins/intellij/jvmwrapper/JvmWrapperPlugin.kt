package ru.itbasis.plugins.intellij.jvmwrapper

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.twelvemonkeys.io.FileUtil.toHumanReadableSize
import ru.itbasis.jvmwrapper.core.ProcessStepListener
import ru.itbasis.jvmwrapper.core.vendor.DownloadProcessListener
import ru.itbasis.jvmwrapper.core.wrapper.JVMW_PROPERTY_FILE_NAME
import ru.itbasis.jvmwrapper.core.wrapper.JvmWrapper
import java.io.File

class JvmWrapperPlugin(project: Project, private val progressIndicator: ProgressIndicator) {

  private val jvmWrapper: JvmWrapper = JvmWrapper(
    workingDir = File(project.baseDir.takeIf { it.findChild(JVMW_PROPERTY_FILE_NAME) != null }!!.canonicalPath),
    stepListener = stepListener(),
    downloadProcessListener = downloadProcessListener()
  )

  private fun stepListener(): ProcessStepListener = { msg ->
    ProgressManager.checkCanceled()

    progressIndicator.run {
      start()
      fraction = 0.0
      text = msg
    }
  }

  private fun downloadProcessListener(): DownloadProcessListener = { url, sizeCurrent, sizeTotal ->
    progressIndicator.run {
      ProgressManager.checkCanceled()
      val percentage = sizeCurrent.toDouble() / sizeTotal
      fraction = percentage
      text = "%s / %s (%.2f%%) < %s".format(toHumanReadableSize(sizeCurrent), toHumanReadableSize(sizeTotal), percentage * 100, url)
      return@run this.isRunning
    }
  }
}
