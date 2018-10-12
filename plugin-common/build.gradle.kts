import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin

apply {
	plugin<KotlinPlatformJvmPlugin>()
}

dependencies {
	"compile"("com.github.itbasis.kotlin-utils:kotlin-utils-jvm")

	"compile"("org.codehaus.plexus:plexus-archiver")
	"compile"("org.codehaus.plexus:plexus-slf4j-logging")

	"compile"("org.apache.commons:commons-lang3")
	"implementation"("org.apache.httpcomponents:httpclient")
	"implementation"("com.google.code.gson:gson")

//	"implementation"("io.ktor:ktor-client-cio")
//	"implementation"("io.ktor:ktor-client-features")
}
