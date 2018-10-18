@file:Suppress("SpellCheckingInspection")

package samples

import ru.itbasis.jvmwrapper.core.downloader.RemoteArchiveFile

var jvmVersionSample__openjdk_jdk_12 = JvmVersionSample(
	vendor = "openjdk",
	type = "jdk",
	versions = listOf("12"),
	fullVersion = "12-ea+15",
	cleanVersion = "12",
	versionMajor = 12,
	versionUpdate = null,
	downloadPageUrl = "https://jdk.java.net/12/",
	downloadArchiveUrlPart = "",
	remoteFiles = mapOf(
		"darwin" to RemoteArchiveFile(
			url = "https://download.java.net/java/early_access/jdk12/14/GPL/openjdk-12-ea+15_osx-x64_bin.tar.gz",
			checksum = "25f8259e2a5ee89933f63c4cb66503a2c6e86994ca3830381a4a0a3711690085"
		), "linux" to RemoteArchiveFile(
		url = "https://download.java.net/java/early_access/jdk12/14/GPL/openjdk-12-ea+15_linux-x64_bin.tar.gz",
		checksum = "d5603ba136d07918e621c7ede56bbda8e76da4656d3d02874fa435a6bd69779f"
	), "windows" to RemoteArchiveFile(
		url = "https://download.java.net/java/early_access/jdk12/14/GPL/openjdk-12-ea+15_windows-x64_bin.zip",
		checksum = "b41b5b427bb51d4e0193ec448ad884be1c67092dc51c5f9bd7521e7b66c60a90"
	), "alpine_linux" to RemoteArchiveFile(
		url = "https://download.java.net/java/early_access/alpine/12/binaries/openjdk-12-ea+12_linux-x64-musl_bin.tar.gz",
		checksum = "172c7d7c6859253822e03f0839f83627ffe06055f118423c6ef619a1af836b4c"
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
		"osx" to RemoteArchiveFile(
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

var jvmVersionSample__openjdk_jdk_11_0_1 = JvmVersionSample(
	vendor = "openjdk",
	type = "jdk",
	versions = listOf("11"),
	fullVersion = "11.0.1+13",
	cleanVersion = "11.0.1",
	versionMajor = 11,
	versionUpdate = null,
	downloadPageUrl = "https://jdk.java.net/11/",
	downloadArchiveUrlPart = "",
	remoteFiles = mapOf(
		"darwin" to RemoteArchiveFile(
			url = "https://download.java.net/java/GA/jdk11/13/GPL/openjdk-11.0.1_osx-x64_bin.tar.gz",
			checksum = "fa07eee08fa0f3de541ee1770de0cdca2ae3876f3bd78c329f27e85c287cd070"
		), "linux" to RemoteArchiveFile(
		url = "https://download.java.net/java/GA/jdk11/13/GPL/openjdk-11.0.1_linux-x64_bin.tar.gz",
		checksum = "7a6bb980b9c91c478421f865087ad2d69086a0583aeeb9e69204785e8e97dcfd"
	), "windows" to RemoteArchiveFile(
		url = "https://download.java.net/java/GA/jdk11/13/GPL/openjdk-11.0.1_windows-x64_bin.zip",
		checksum = "289dd06e06c2cbd5e191f2d227c9338e88b6963fd0c75bceb9be48f0394ede21"
	)
	)
)

val OpenJDKJvmVersionEarlyAccessSamples = listOf(
	jvmVersionSample__openjdk_jdk_12
)

val OpenJDKJvmVersionLatestSamples = listOf(
	jvmVersionSample__openjdk_jdk_11_0_1
)

val OpenJDKJvmVersionArchiveSamples = listOf(
	jvmVersionSample__openjdk_jdk_11
)