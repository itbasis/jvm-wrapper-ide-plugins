package ru.itbasis.jvmwrapper.core.jvm.detectors

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import mu.KotlinLogging
import ru.itbasis.jvmwrapper.core.AbstractIntegrationTests
import java.io.File

class JvmVendorDetectorIntegrationTest : AbstractIntegrationTests() {
	override val logger = KotlinLogging.logger {}

	init {
		test("detect").config(enabled = false) {
			forall(
				rows = *jvmAllRows
			) { jvmVersionRow ->
				val jvm = downloadVersion(jvmVersionSample = jvmVersionRow)

				val jvmHomePath = File(temporaryJvmWrapperFolder(), jvm.toString())
				JvmVendorDetector.detect(path = jvmHomePath.absolutePath).name shouldBe jvmVersionRow.vendor.toUpperCase()
			}
		}
	}
}