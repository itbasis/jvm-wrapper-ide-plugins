package ru.itbasis.jvmwrapper.core

import asRows
import io.kotlintest.specs.FunSpec
import mu.KLogger
import ru.itbasis.jvmwrapper.core.downloader.DownloadProcessListener
import samples.OpenJDKJvmVersionEarlyAccessSamples
import samples.OpenJDKJvmVersionLatestSamples
import samples.OracleJvmVersionArchiveSamples
import samples.OracleJvmVersionLatestSamples
import samples.jvmVersionSample__openjdk_jdk_11
import java.math.BigDecimal
import java.math.RoundingMode

abstract class AbstractIntegrationTests : FunSpec() {
	abstract val logger: KLogger

	protected open val jvmAllRows = listOf(
		OpenJDKJvmVersionLatestSamples, OracleJvmVersionLatestSamples, OracleJvmVersionArchiveSamples, OpenJDKJvmVersionEarlyAccessSamples
	).flatten().asRows()

	protected open val jvmFirstRows = listOf(
		jvmVersionSample__openjdk_jdk_11
//		OpenJDKJvmVersionLatestSamples.firstOrNull(),
//		OracleJvmVersionLatestSamples.firstOrNull(),
//		OracleJvmVersionArchiveSamples.firstOrNull(),
//		OpenJDKJvmVersionEarlyAccessSamples.firstOrNull()
	).asRows()

	override fun isInstancePerTest(): Boolean {
		return true
	}

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
