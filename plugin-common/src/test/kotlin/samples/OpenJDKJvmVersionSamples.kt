@file:Suppress("SpellCheckingInspection")

package samples

import ru.itbasis.jvmwrapper.core.FileNameExtension.DMG
import ru.itbasis.jvmwrapper.core.FileNameExtension.EXE
import ru.itbasis.jvmwrapper.core.FileNameExtension.TAR_GZ
import ru.itbasis.jvmwrapper.core.FileNameExtension.ZIP
import ru.itbasis.jvmwrapper.core.OsType.ALPINE_LINUX
import ru.itbasis.jvmwrapper.core.OsType.LINUX
import ru.itbasis.jvmwrapper.core.OsType.OSX
import ru.itbasis.jvmwrapper.core.OsType.WINDOWS
import ru.itbasis.jvmwrapper.core.downloader.RemoteArchiveFile

var jvmVersionSample__openjdk_jdk_12 = JvmVersionSample(
	vendor = "openjdk",
	type = "jdk",
	versions = listOf("12"),
	fullVersion = "12-ea+20",
	cleanVersion = "12",
	versionMajor = 12,
	versionUpdate = null,
	versionEarlyAccess = true,
	downloadPageUrl = "https://jdk.java.net/12/",
	downloadArchiveUrlPart = "",
	remoteFiles = mapOf(
		LINUX to RemoteArchiveFile(
			url = "https://download.java.net/java/early_access/jdk12/20/GPL/openjdk-12-ea+20_linux-x64_bin.tar.gz",
			checksum = "a1d61eef989c876f241829b979ac66232e08f55c3c3a3ee618c178e79ede9cff",
			archiveFileExtension = TAR_GZ
		), OSX to RemoteArchiveFile(
		url = "https://download.java.net/java/early_access/jdk12/20/GPL/openjdk-12-ea+20_osx-x64_bin.tar.gz",
		checksum = "50c20a603364d3f0ef9378f7ac2bfd0f7ea8de376789e5ab834b57a946384fb3",
		archiveFileExtension = TAR_GZ
	), WINDOWS to RemoteArchiveFile(
		url = "https://download.java.net/java/early_access/jdk12/20/GPL/openjdk-12-ea+20_windows-x64_bin.zip",
		checksum = "42f843635a0166951bd96a85d982ead987b6cfc07ba18b7f6830eb5a6c1df5c9",
		archiveFileExtension = ZIP
	), ALPINE_LINUX to RemoteArchiveFile(
		url = "https://download.java.net/java/early_access/alpine/20/binaries/openjdk-12-ea+20_linux-x64-musl_bin.tar.gz",
		checksum = "125162b84369be592f8355624075f579795343a835706f0cbde065882d9404a1",
		archiveFileExtension = TAR_GZ
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
		LINUX to RemoteArchiveFile(
			url = "https://download.java.net/java/GA/jdk11/13/GPL/openjdk-11.0.1_linux-x64_bin.tar.gz",
			checksum = "7a6bb980b9c91c478421f865087ad2d69086a0583aeeb9e69204785e8e97dcfd",
			archiveFileExtension = TAR_GZ
		), OSX to RemoteArchiveFile(
		url = "https://download.java.net/java/GA/jdk11/13/GPL/openjdk-11.0.1_osx-x64_bin.tar.gz",
		checksum = "fa07eee08fa0f3de541ee1770de0cdca2ae3876f3bd78c329f27e85c287cd070",
		archiveFileExtension = TAR_GZ
	), WINDOWS to RemoteArchiveFile(
		url = "https://download.java.net/java/GA/jdk11/13/GPL/openjdk-11.0.1_windows-x64_bin.zip",
		checksum = "289dd06e06c2cbd5e191f2d227c9338e88b6963fd0c75bceb9be48f0394ede21",
		archiveFileExtension = ZIP
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
		OSX to RemoteArchiveFile(
			url = "https://download.java.net/java/ga/jdk11/openjdk-11_osx-x64_bin.tar.gz",
			checksum = "6b969d2df6a9758d9458f5ba47939250e848dfba8b49e41c935cf210606b6d38",
			archiveFileExtension = TAR_GZ
		), LINUX to RemoteArchiveFile(
		url = "https://download.java.net/java/ga/jdk11/openjdk-11_linux-x64_bin.tar.gz",
		checksum = "3784cfc4670f0d4c5482604c7c513beb1a92b005f569df9bf100e8bef6610f2e",
		archiveFileExtension = TAR_GZ
	), WINDOWS to RemoteArchiveFile(
		url = "https://download.java.net/java/ga/jdk11/openjdk-11_windows-x64_bin.zip",
		checksum = "fde3b28ca31b86a889c37528f17411cd0b9651beb6fa76cac89a223417910f4b",
		archiveFileExtension = ZIP
	)
	)
)

var jvmVersionSample__openjdk_jdk_8 = JvmVersionSample(
	vendor = "openjdk",
	type = "jdk",
	versions = listOf("8", "8u202"),
	fullVersion = "1.8.0_202-ea-b03",
	cleanVersion = "8u202",
	versionMajor = 8,
	versionUpdate = 202,
	versionEarlyAccess = true,
	downloadPageUrl = "https://jdk.java.net/8/",
	downloadArchiveUrlPart = "",
	remoteFiles = mapOf(
		LINUX to RemoteArchiveFile(
			url = "https://download.java.net/java/early_access/jdk8/b03/BCL/jdk-8u202-ea-bin-b03-linux-x64-07_nov_2018.tar.gz",
			archiveFileExtension = TAR_GZ
		), OSX to RemoteArchiveFile(
		url = "https://download.java.net/java/early_access/jdk8/b03/BCL/jdk-8u202-ea-bin-b03-macosx-x86_64-07_nov_2018.dmg",
		archiveFileExtension = DMG
	), WINDOWS to RemoteArchiveFile(
		url = "https://download.java.net/java/early_access/jdk8/b03/BCL/jdk-8u202-ea-bin-b03-windows-x64-07_nov_2018.exe",
		archiveFileExtension = EXE
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