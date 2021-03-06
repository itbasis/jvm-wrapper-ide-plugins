package ru.itbasis.jvmwrapper.core.actions

import io.kotlintest.Description
import io.kotlintest.TestResult
import io.kotlintest.matchers.file.shouldBeNonEmptyDirectory
import io.kotlintest.matchers.file.shouldNotBeNonEmptyDirectory
import io.kotlintest.specs.FunSpec
import org.junit.rules.TemporaryFolder

internal class GenerateWrapperActionTest : FunSpec() {
	private var temporaryFolder = TemporaryFolder()

	override fun beforeTest(description: Description) {
		temporaryFolder.create()
	}

	override fun afterTest(description: Description, result: TestResult) {
		temporaryFolder.delete()
	}

	init {
		test("generate wrapper") {
			val workDir = temporaryFolder.root
			workDir.shouldNotBeNonEmptyDirectory()

			GenerateWrapperAction(parentDir = workDir).run()
			workDir.shouldBeNonEmptyDirectory()
		}
	}
}
