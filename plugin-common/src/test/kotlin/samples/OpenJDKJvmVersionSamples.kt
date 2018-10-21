@file:Suppress("SpellCheckingInspection")

package samples

import ru.itbasis.jvmwrapper.core.downloader.RemoteArchiveFile

var jvmVersionSample__openjdk_jdk_12 = JvmVersionSample(
	vendor = "openjdk",
	type = "jdk",
	versions = listOf("12"),
	fullVersion = "12-ea+16",
	cleanVersion = "12",
	versionMajor = 12,
	versionUpdate = null,
	versionEarlyAccess = true,
	downloadPageUrl = "https://jdk.java.net/12/",
	downloadArchiveUrlPart = "",
	remoteFiles = mapOf(
		"osx" to RemoteArchiveFile(
			url = "https://download.java.net/java/early_access/jdk12/16/GPL/openjdk-12-versionEarlyAccess+16_osx-x64_bin.tar.gz",
			checksum = "557e8259050ae9aa463dd8373d02e34deaae07db1b7fd3b9b7f16c81477e7bb7"
		), "linux" to RemoteArchiveFile(
		url = "https://download.java.net/java/early_access/jdk12/16/GPL/openjdk-12-versionEarlyAccess+16_linux-x64_bin.tar.gz",
		checksum = "20ab4c21b20d0c823c5dcbf0bdb81bc9ab5c2a45954a80563ffd591b25b36038"
	), "windows" to RemoteArchiveFile(
		url = "https://download.java.net/java/early_access/jdk12/16/GPL/openjdk-12-versionEarlyAccess+16_windows-x64_bin.zip",
		checksum = "cd9cd3087d1c11912662870d62257c200eb82c407eb9d364d1ee86d65711c2b4"
	), "alpine_linux" to RemoteArchiveFile(
		url = "https://download.java.net/java/early_access/alpine/14/binaries/openjdk-12-versionEarlyAccess+14_linux-x64-musl_bin.tar.gz",
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
	versions = listOf("11.0.1"),
	fullVersion = "11.0.1+13",
	cleanVersion = "11.0.1",
	versionMajor = 11,
	versionUpdate = 1,
	downloadPageUrl = "https://jdk.java.net/11/",
	downloadArchiveUrlPart = "",
	remoteFiles = mapOf(
		"osx" to RemoteArchiveFile(
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

var jvmVersionSample__openjdk_jdk_8 = JvmVersionSample(
	vendor = "openjdk",
	type = "jdk",
	versions = listOf("8", "8u192"),
	fullVersion = "1.8.0_192-ea-b04",
	cleanVersion = "8u192",
	versionMajor = 8,
	versionUpdate = 192,
	versionEarlyAccess = true,
	downloadPageUrl = "https://jdk.java.net/8/",
	downloadArchiveUrlPart = "",
	remoteFiles = mapOf(
		"osx" to RemoteArchiveFile(
			url = "https://download.java.net/java/jdk8u192/archive/b04/binaries/jdk-8u192-ea-bin-b04-macosx-x86_64-01_aug_2018.dmg"
		), "linux" to RemoteArchiveFile(
		url = "https://download.java.net/java/jdk8u192/archive/b04/binaries/jdk-8u192-ea-bin-b04-linux-x64-01_aug_2018.tar.gz"
	), "windows" to RemoteArchiveFile(
		url = "https://download.java.net/java/jdk8u192/archive/b04/binaries/jdk-8u192-ea-bin-b04-windows-x64-01_aug_2018.exe"
	)
	)
)

val OpenJDKJvmVersionEarlyAccessSamples = listOf(
	jvmVersionSample__openjdk_jdk_12, jvmVersionSample__openjdk_jdk_8
)

val OpenJDKJvmVersionLatestSamples = listOf(
	jvmVersionSample__openjdk_jdk_11_0_1
)

val OpenJDKJvmVersionArchiveSamples = listOf(
	jvmVersionSample__openjdk_jdk_11
)