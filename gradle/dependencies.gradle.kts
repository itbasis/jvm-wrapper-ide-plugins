import org.gradle.plugins.ide.idea.model.IdeaModel

val kotlinVersion: String by extra

repositories {
  mavenLocal()
  jcenter()
  mavenCentral()
  maven(url = "https://jitpack.io")
}

configurations.all {
  resolutionStrategy {
    failOnVersionConflict()

    eachDependency {
      when (requested.group) {
        "org.slf4j" -> useVersion("1.7.25")
        "junit" -> useVersion("4.12")
        "org.junit.platform" -> useVersion("1.2.0")
        "org.junit.jupiter" -> useVersion("5.2.0")
        "org.mockito" -> useVersion("2.21.0")
        "org.opentest4j" -> useVersion("1.1.0")
        "org.objenesis" -> useVersion("2.6")
        "org.jetbrains.kotlin" -> useVersion(kotlinVersion)
        "io.kotlintest" -> useVersion("3.1.9")
        "io.github.glytching" -> useVersion("2.3.0")
        "org.apache.commons" -> when (requested.name) {
          "commons-lang3" -> useVersion("3.4")
          "commons-compress" -> useVersion("1.17")
        }
        "org.apache.httpcomponents" -> when (requested.name) {
          "httpclient" -> useVersion("4.5.6")
          "httpcore" -> useVersion("4.4.10")
        }
        "com.google.code.gson" -> useVersion("2.8.5")
        "io.github.microutils" -> useVersion("1.6.10")
        "com.github.itbasis.kotlin-utils" -> useVersion("v20180618_1139")
      }
    }
  }
}

apply {
  plugin<IdeaPlugin>()
}

configure<IdeaModel> {
  module {
    isDownloadJavadoc = false
    isDownloadSources = false
  }
}
