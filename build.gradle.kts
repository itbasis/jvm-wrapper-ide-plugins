import com.gradle.scan.plugin.BuildScanExtension
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformPluginBase
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import java.time.LocalDateTime
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.utils.addToStdlib.ifNotEmpty
import java.time.format.DateTimeFormatter

tasks.withType<Wrapper> {
	distributionType = Wrapper.DistributionType.BIN
	gradleVersion = "5.0-rc-3"
}

group = "ru.itbasis.jvm-wrapper"

buildscript {
	val kotlinVersion: String by extra

	repositories {
		jcenter()
		gradlePluginPortal()
	}
	dependencies {
		classpath(kotlin("gradle-plugin", kotlinVersion))
		classpath("gradle.plugin.io.gitlab.arturbosch.detekt:detekt-gradle-plugin:+")
	}
}

plugins {
	`build-scan`
}

apply {
	plugin<IdeaPlugin>()
}

configure<BuildScanExtension> {
	setTermsOfServiceUrl("https://gradle.com/terms-of-service")
	setTermsOfServiceAgree("yes")

	if (!System.getenv("CI").isNullOrEmpty()) {
		publishAlways()
		tag("CI")
	}
}

allprojects {

	version = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"))

	apply {
		from("$rootDir/gradle/dependencies.gradle.kts")
		plugin<BasePlugin>()
		plugin<DetektPlugin>()
	}

	configure<DetektExtension> {
		config = files(rootDir.resolve("detekt-config.yml"))
	}

	afterEvaluate {
		plugins.withType(JavaBasePlugin::class.java) {
			configure<JavaPluginConvention> {
				sourceCompatibility = JavaVersion.VERSION_1_8
			}
			tasks.withType(Detekt::class.java) {
				dependsOn(tasks.withType(Test::class.java))
				tasks.getByName(JavaBasePlugin.CHECK_TASK_NAME).dependsOn(this)
			}
//      plugins.withType(NebulaFacetPlugin::class.java) {
//        TestFacetDefinition("integrationTest").also {
//          extension.add(it)
//        }
//      }
		}

		tasks.withType(KotlinCompile::class.java) {
			kotlinOptions {
				jvmTarget = JavaVersion.VERSION_1_8.toString()
			}
		}

		plugins.withType(KotlinPlatformPluginBase::class.java) {
//			configure<KotlinProjectExtension> {
//				experimental.coroutines = Coroutines.ENABLE
//			}
			dependencies {
				"compile"(kotlin("stdlib-jdk8"))
				"implementation"("io.github.microutils:kotlin-logging") {
					exclude(group = "org.slf4j")
				}
				"compileOnly"("org.slf4j:slf4j-api")

				"testImplementation"("org.slf4j:slf4j-log4j12")

				arrayOf(
					kotlin("test-junit"),
					"io.kotlintest:kotlintest-extensions-system",
					"io.kotlintest:kotlintest-assertions-arrow",
					"io.kotlintest:kotlintest-runner-junit4"
				).forEach {
					"testCompile"(it)
//          "integrationTestCompile"(it)
				}
			}
		}

		tasks.withType(Test::class.java).all {
			failFast = true
			useJUnit()
			testLogging {
				showStandardStreams = true
			}
		}
	}
}
