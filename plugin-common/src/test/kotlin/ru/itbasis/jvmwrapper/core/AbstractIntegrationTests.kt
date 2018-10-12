package ru.itbasis.jvmwrapper.core

import io.kotlintest.specs.FunSpec
import mu.KLogger
import ru.itbasis.jvmwrapper.core.downloader.DownloadProcessListener
import java.math.BigDecimal
import java.math.RoundingMode

abstract class AbstractIntegrationTests : FunSpec() {
	abstract val logger: KLogger

	protected val stepListener: (String) -> Unit = { msg ->
		logger.info { msg }
	}

	protected val downloadProcessListener: DownloadProcessListener? = if (launchedInCI) {
		null
	} else {
		{ _, sizeCurrent, sizeTotal ->
			val percentage = BigDecimal(sizeCurrent.toDouble() / sizeTotal * 100).setScale(2, RoundingMode.HALF_UP)
			logger.info { "$sizeCurrent / $sizeTotal :: $percentage%" }
			true
		}
	}

	companion object {
		val launchedInCI = System.getenv().containsKey("CI")
	}
}
