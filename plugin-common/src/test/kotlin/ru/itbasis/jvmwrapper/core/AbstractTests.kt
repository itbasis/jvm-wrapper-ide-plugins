package ru.itbasis.jvmwrapper.core

import io.kotlintest.specs.FunSpec
import samples.OpenJDKJvmVersionEarlyAccessSamples
import samples.OpenJDKJvmVersionLatestSamples
import samples.OracleJvmVersionArchiveSamples
import samples.OracleJvmVersionLatestSamples
import samples.asKotlinTestRows

abstract class AbstractTests : FunSpec() {
	protected open val jvmAllRows = listOf(
//		OpenJDKJvmVersionLatestSamples, OracleJvmVersionLatestSamples, OracleJvmVersionArchiveSamples,
		OpenJDKJvmVersionEarlyAccessSamples
	).flatten().asKotlinTestRows()

	protected open val jvmFirstRows = listOf(
//		OpenJDKJvmVersionLatestSamples.firstOrNull(),
//		OracleJvmVersionLatestSamples.firstOrNull(),
//		OracleJvmVersionArchiveSamples.firstOrNull(),
		OpenJDKJvmVersionEarlyAccessSamples.firstOrNull()
	).asKotlinTestRows()

	override fun isInstancePerTest(): Boolean {
		return true
	}
}
