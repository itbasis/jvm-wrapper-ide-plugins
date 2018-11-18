package samples

import ru.itbasis.jvmwrapper.core.FileNameExtension.DMG
import ru.itbasis.jvmwrapper.core.FileNameExtension.EXE
import ru.itbasis.jvmwrapper.core.FileNameExtension.TAR_GZ
import ru.itbasis.jvmwrapper.core.FileNameExtension.ZIP
import ru.itbasis.jvmwrapper.core.OsType.LINUX
import ru.itbasis.jvmwrapper.core.OsType.OSX
import ru.itbasis.jvmwrapper.core.OsType.WINDOWS
import ru.itbasis.jvmwrapper.core.downloader.RemoteArchiveFile

val jvmVersionSample__oracle_jdk_11_0_1 = JvmVersionSample(
	vendor = "oracle",
	type = "jdk",
	versions = listOf("11.0.1"),
	fullVersion = "11.0.1+13-LTS",
	cleanVersion = "11.0.1",
	versionMajor = 11,
	versionUpdate = 1,
	downloadPageUrl = "/technetwork/java/javase/downloads/jdk11-downloads-5066655.html",
	downloadArchiveUrlPart = "http://download.oracle.com/otn-pub/java/jdk/11.0.1+13/90cf5d8f270a4347a95050320eef3fb7/jdk-11.0.1_",
	remoteFiles = mapOf(
		LINUX to RemoteArchiveFile(
			url = "http://download.oracle.com/otn-pub/java/jdk/11.0.1+13/90cf5d8f270a4347a95050320eef3fb7/jdk-11.0.1_linux-x64_bin.tar.gz",
			checksum = "e7fd856bacad04b6dbf3606094b6a81fa9930d6dbb044bbd787be7ea93abc885",
			archiveFileExtension = TAR_GZ
		), OSX to RemoteArchiveFile(
		url = "http://download.oracle.com/otn-pub/java/jdk/11.0.1+13/90cf5d8f270a4347a95050320eef3fb7/jdk-11.0.1_osx-x64_bin.dmg",
		checksum = "467fd06212438539078c56f768c42c41ba8b26cea1213991da5ba44a601af628",
		archiveFileExtension = DMG
	), WINDOWS to RemoteArchiveFile(
		url = "http://download.oracle.com/otn-pub/java/jdk/11.0.1+13/90cf5d8f270a4347a95050320eef3fb7/jdk-11.0.1_windows-x64_bin.zip",
		checksum = "0bdc75f64e07387e3d303684041898bffd0bb60e8e2cce84cb152042026ed5a7",
		archiveFileExtension = ZIP
	)
	)
)
val jvmVersionSample__oracle_jdk_11 = JvmVersionSample(
	vendor = "oracle",
	type = "jdk",
	versions = listOf("11"),
	fullVersion = "11+28",
	cleanVersion = "11",
	versionMajor = 11,
	versionUpdate = null,
	downloadPageUrl = "/technetwork/java/javase/downloads/java-archive-javase11-5116896.html",
	downloadArchiveUrlPart = "http://download.oracle.com/otn/java/jdk/11+28/55eed80b163941c8885ad9298e6d786a/jdk-11_",
	remoteFiles = mapOf(
		LINUX to RemoteArchiveFile(
			url = "http://download.oracle.com/otn/java/jdk/11+28/55eed80b163941c8885ad9298e6d786a/jdk-11_linux-x64_bin.tar.gz",
			checksum = "246a0eba4927bf30180c573b73d55fc10e226c05b3236528c3a721dff3b50d32",
			archiveFileExtension = TAR_GZ
		), OSX to RemoteArchiveFile(
		url = "http://download.oracle.com/otn/java/jdk/11+28/55eed80b163941c8885ad9298e6d786a/jdk-11_osx-x64_bin.dmg",
		checksum = "aa5fea6e2f009e63dd8a8d7f532104e6476195a49ee6dd0d2b11c64966d028cc",
		archiveFileExtension = DMG
	), WINDOWS to RemoteArchiveFile(
		url = "http://download.oracle.com/otn/java/jdk/11+28/55eed80b163941c8885ad9298e6d786a/jdk-11_windows-x64_bin.zip",
		checksum = "d64b9d725f0ed096ae839ef8506deb3b6f509b2e3ee1f9b0792c5116623d4c9d",
		archiveFileExtension = ZIP
	)
	)
)

val jvmVersionSample__oracle_jdk_10_0_2 = JvmVersionSample(
	vendor = "oracle",
	type = "jdk",
	versions = listOf("10.0.2"),
	fullVersion = "10.0.2+13",
	cleanVersion = "10.0.2",
	versionMajor = 10,
	versionUpdate = 2,
	downloadPageUrl = "/technetwork/java/javase/downloads/java-archive-javase10-4425482.html",
	downloadArchiveUrlPart = "http://download.oracle.com/otn/java/jdk/10.0.2+13/19aef61b38124481863b1413dce1855f/jdk-10.0.2_",
	remoteFiles = mapOf(
		LINUX to RemoteArchiveFile(
			url = "http://download.oracle.com/otn/java/jdk/10.0.2+13/19aef61b38124481863b1413dce1855f/jdk-10.0.2_linux-x64_bin.tar.gz",
			checksum = "6633c20d53c50c20835364d0f3e172e0cbbce78fff81867488f22a6298fa372b",
			archiveFileExtension = TAR_GZ
		), OSX to RemoteArchiveFile(
		url = "http://download.oracle.com/otn/java/jdk/10.0.2+13/19aef61b38124481863b1413dce1855f/jdk-10.0.2_osx-x64_bin.dmg",
		checksum = "2db323c9c93e7fb63e2ed7e06ce8150c32d782e3d0704be6274ebb2d298193aa",
		archiveFileExtension = DMG
	), WINDOWS to RemoteArchiveFile(
		url = "http://download.oracle.com/otn/java/jdk/10.0.2+13/19aef61b38124481863b1413dce1855f/jdk-10.0.2_windows-x64_bin.exe",
		checksum = "bd2aa173db14789ac0369ab32bf929679760cae9e04d751d5f914ac3ad36c129",
		archiveFileExtension = ZIP
	)
	)
)

val jvmVersionSample__oracle_jdk_10_0_1 = JvmVersionSample(
	vendor = "oracle",
	type = "jdk",
	versions = listOf("10.0.1"),
	fullVersion = "10.0.1+10",
	cleanVersion = "10.0.1",
	versionMajor = 10,
	versionUpdate = 1,
	downloadPageUrl = "/technetwork/java/javase/downloads/java-archive-javase10-4425482.html",
	downloadArchiveUrlPart = "http://download.oracle.com/otn/java/jdk/10.0.1+10/fb4372174a714e6b8c52526dc134031e/jdk-10.0.1_",
	remoteFiles = mapOf(
		LINUX to RemoteArchiveFile(
			url = "http://download.oracle.com/otn/java/jdk/10.0.1+10/fb4372174a714e6b8c52526dc134031e/jdk-10.0.1_linux-x64_bin.tar.gz",
			checksum = "ae8ed645e6af38432a56a847597ac61d4283b7536688dbab44ab536199d1e5a4",
			archiveFileExtension = TAR_GZ
		), OSX to RemoteArchiveFile(
		url = "http://download.oracle.com/otn/java/jdk/10.0.1+10/fb4372174a714e6b8c52526dc134031e/jdk-10.0.1_osx-x64_bin.dmg",
		checksum = "cf3d33be870788eed5bb5eeef8f52aa9d7601955c8742efbec0cf9fbd6245ceb",
		archiveFileExtension = DMG
	), WINDOWS to RemoteArchiveFile(
		url = "http://download.oracle.com/otn/java/jdk/10.0.1+10/fb4372174a714e6b8c52526dc134031e/jdk-10.0.1_windows-x64_bin.exe",
		checksum = "9917df90549c8ebc0430dfe92478091ecff05e1a10edc708657e83a987727695",
		archiveFileExtension = ZIP
	)
	)
)

val jvmVersionSample__oracle_jdk_8u191 = JvmVersionSample(
	vendor = "oracle",
	type = "jdk",
	versions = listOf("8u191", "1.8.0_191-b12"),
	fullVersion = "1.8.0_191-b12",
	cleanVersion = "8u191",
	versionMajor = 8,
	versionUpdate = 191,
	downloadPageUrl = "/technetwork/java/javase/downloads/jdk8-downloads-2133151.html",
	downloadArchiveUrlPart = "http://download.oracle.com/otn-pub/java/jdk/8u191-b12/2787e4a523244c269598db4e85c51e0c/jdk-8u191-",
	remoteFiles = mapOf(
		LINUX to RemoteArchiveFile(
			url = "http://download.oracle.com/otn-pub/java/jdk/8u191-b12/2787e4a523244c269598db4e85c51e0c/jdk-8u191-linux-x64.tar.gz",
			checksum = "53c29507e2405a7ffdbba627e6d64856089b094867479edc5ede4105c1da0d65",
			archiveFileExtension = TAR_GZ
		), OSX to RemoteArchiveFile(
		url = "http://download.oracle.com/otn-pub/java/jdk/8u191-b12/2787e4a523244c269598db4e85c51e0c/jdk-8u191-macosx-x64.dmg",
		checksum = "5e33a6bfc1c413d647f1ac37eb0d1eb9ca3ee6e4600898eab81338e8f5d10e13",
		archiveFileExtension = DMG
	), WINDOWS to RemoteArchiveFile(
		url = "http://download.oracle.com/otn-pub/java/jdk/8u191-b12/2787e4a523244c269598db4e85c51e0c/jdk-8u191-windows-x64.exe",
		checksum = "a2b1d99cf026880869d8188872e20f615133e7a40786712365d7b750f1dd6e57",
		archiveFileExtension = EXE
	)
	)
)

val jvmVersionSample__oracle_jdk_8u172 = JvmVersionSample(
	vendor = "oracle",
	type = "jdk",
	versions = listOf("8u172", "1.8.0_172-b11"),
	fullVersion = "1.8.0_172-b11",
	cleanVersion = "8u172",
	versionMajor = 8,
	versionUpdate = 172,
	downloadPageUrl = "/technetwork/java/javase/downloads/java-archive-javase8-2177648.html",
	downloadArchiveUrlPart = "http://download.oracle.com/otn/java/jdk/8u172-b11/a58eab1ec242421181065cdc37240b08/jdk-8u172-",
	remoteFiles = mapOf(
		LINUX to RemoteArchiveFile(
			url = "http://download.oracle.com/otn/java/jdk/8u172-b11/a58eab1ec242421181065cdc37240b08/jdk-8u172-linux-x64.tar.gz",
			checksum = "28a00b9400b6913563553e09e8024c286b506d8523334c93ddec6c9ec7e9d346",
			archiveFileExtension = TAR_GZ
		), OSX to RemoteArchiveFile(
		url = "http://download.oracle.com/otn/java/jdk/8u172-b11/a58eab1ec242421181065cdc37240b08/jdk-8u172-macosx-x64.dmg",
		checksum = "b0de04d3ec7fbf2e54e33e29c78ababa0a4df398ba490d4abb125b31ea8d663e",
		archiveFileExtension = DMG
	), WINDOWS to RemoteArchiveFile(
		url = "http://download.oracle.com/otn/java/jdk/8u172-b11/a58eab1ec242421181065cdc37240b08/jdk-8u172-windows-x64.exe",
		checksum = "0b330b00576420a38f5c76cd07899b46551c075fa9e4df6028b14828e538e30d",
		archiveFileExtension = EXE
	)
	)
)

val jvmVersionSample__oracle_jdk_8u171 = JvmVersionSample(
	vendor = "oracle",
	type = "jdk",
	versions = listOf("8u171", "1.8.0_171-b11"),
	fullVersion = "1.8.0_171-b11",
	cleanVersion = "8u171",
	versionMajor = 8,
	versionUpdate = 171,
	downloadPageUrl = "/technetwork/java/javase/downloads/java-archive-javase8-2177648.html",
	downloadArchiveUrlPart = "http://download.oracle.com/otn/java/jdk/8u171-b11/512cd62ec5174c3487ac17c61aaa89e8/jdk-8u171-",
	remoteFiles = mapOf(
		LINUX to RemoteArchiveFile(
			url = "http://download.oracle.com/otn/java/jdk/8u171-b11/512cd62ec5174c3487ac17c61aaa89e8/jdk-8u171-linux-x64.tar.gz",
			checksum = "b6dd2837efaaec4109b36cfbb94a774db100029f98b0d78be68c27bec0275982",
			archiveFileExtension = TAR_GZ
		), OSX to RemoteArchiveFile(
		url = "http://download.oracle.com/otn/java/jdk/8u171-b11/512cd62ec5174c3487ac17c61aaa89e8/jdk-8u171-macosx-x64.dmg",
		checksum = "00ccc048009e64e7e341d55d35c8519ab63ef5f86f0d73d4e823281d0b358d40",
		archiveFileExtension = DMG
	), WINDOWS to RemoteArchiveFile(
		url = "http://download.oracle.com/otn/java/jdk/8u171-b11/512cd62ec5174c3487ac17c61aaa89e8/jdk-8u171-windows-x64.exe",
		checksum = "841b20e904b7f46fe7c8ce88bd35148e9663c750c8336286d0eb05a0a5b203f4",
		archiveFileExtension = EXE
	)
	)
)

val OracleJvmVersionLatestSamples = listOf(
	jvmVersionSample__oracle_jdk_11_0_1, jvmVersionSample__oracle_jdk_8u191
)

val OracleJvmVersionArchiveSamples = listOf(
	jvmVersionSample__oracle_jdk_11,
	jvmVersionSample__oracle_jdk_10_0_2,
	jvmVersionSample__oracle_jdk_10_0_1,
	jvmVersionSample__oracle_jdk_8u171,
	jvmVersionSample__oracle_jdk_8u172
)