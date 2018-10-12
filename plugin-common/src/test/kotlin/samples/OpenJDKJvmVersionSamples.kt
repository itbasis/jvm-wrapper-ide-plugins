@file:Suppress("SpellCheckingInspection")

package samples

import ru.itbasis.jvmwrapper.core.downloader.RemoteArchiveFile

var jvmVersionSample__openjdk_jdk_12 = JvmVersionSample(
	vendor = "openjdk",
	type = "jdk",
	versions = listOf("12"),
	fullVersion = "12-ea+14",
	cleanVersion = "12",
	versionMajor = 12,
	versionUpdate = null,
	downloadPageUrl = "https://jdk.java.net/12/",
	downloadArchiveUrlPart = "",
	remoteFiles = mapOf(
		"darwin" to RemoteArchiveFile(
			url = "https://download.java.net/java/early_access/jdk12/14/GPL/openjdk-12-ea+14_osx-x64_bin.tar.gz",
			checksum = "8f91596d4977be9bc3487bfe9321ae00737995bc69fd8b31d3c875022e1a173a"
		), "linux" to RemoteArchiveFile(
		url = "https://download.java.net/java/early_access/jdk12/14/GPL/openjdk-12-ea+14_linux-x64_bin.tar.gz",
		checksum = "791ac6f6494a587a4de9dc373cdaa3911dc25ad4c7e793fb8e4dbe1269ab534d"
	), "windows" to RemoteArchiveFile(
		url = "https://download.java.net/java/early_access/jdk12/14/GPL/openjdk-12-ea+14_windows-x64_bin.zip",
		checksum = "43ac13c36e6d868c55e4e5bfefa2e9ae1fa7fb4006fde49ac8b2a3e1b9ca8279"
	), "alpine_linux" to RemoteArchiveFile(
		url = "https://download.java.net/java/early_access/alpine/12/binaries/openjdk-12-ea+12_linux-x64-musl_bin.tar.gz",
		checksum = "36729ff2deec675c87fefbee47b805ad1555192f424ca4debeb81f30eb1bf587"
	)
	)
)

var jvmVersionSample__openjdk_jdk_11 = JvmVersionSample(
	vendor = "openjdk",
	type = "jdk",
	versions = listOf("11"),
	fullVersion = "11+28",
	cleanVersion = "11",
	versionMajor = 11,
	versionUpdate = null,
	downloadPageUrl = "https://jdk.java.net/11/",
	downloadArchiveUrlPart = "",
	remoteFiles = mapOf(
		"darwin" to RemoteArchiveFile(
			url = "https://download.java.net/java/ga/jdk11/openjdk-11_osx-x64_bin.tar.gz",
			checksum = "6b969d2df6a9758d9458f5ba47939250e848dfba8b49e41c935cf210606b6d38"
		), "linux" to RemoteArchiveFile(
		url = "https://download.java.net/java/ga/jdk11/openjdk-11_linux-x64_bin.tar.gz",
		checksum = "3784cfc4670f0d4c5482604c7c513beb1a92b005f569df9bf100e8bef6610f2e"
	), "windows" to RemoteArchiveFile(
		url = "https://download.java.net/java/ga/jdk11/openjdk-11_windows-x64_bin.zip",
		checksum = "fde3b28ca31b86a889c37528f17411cd0b9651beb6fa76cac89a223417910f4b"
	)
	)
)

val OpenJDKJvmVersionEarlyAccessSamples = listOf(
	jvmVersionSample__openjdk_jdk_12
)

val OpenJDKJvmVersionLatestSamples = listOf(
	jvmVersionSample__openjdk_jdk_11
)