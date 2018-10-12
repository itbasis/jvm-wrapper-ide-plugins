package ru.itbasis.jvmwrapper.core

import io.kotlintest.specs.FunSpec
import mu.KLogger
import java.math.BigDecimal
import java.math.RoundingMode

abstract class AbstractIntegrationTests : FunSpec() {
	abstract val logger: KLogger

	protected val stepListener: (String) -> Unit = { msg -> logger.info { msg } }
	protected val downloadProcessListener: (String, Long, Long) -> Boolean = { _, sizeCurrent, sizeTotal ->
		val percentage = BigDecimal(sizeCurrent.toDouble() / sizeTotal * 100).setScale(2, RoundingMode.HALF_UP)
		logger.info { "$sizeCurrent / $sizeTotal :: $percentage%" }
		true
	}

	companion object {
		fun launchedInCI() =
			System.getenv().containsKey("CI")
	}
}
