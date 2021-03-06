package samples

val jvmVersionSample__oracle_jdk_11_0_1 = JvmVersionSample(
	vendor = "oracle",
	type = "jdk",
	versions = listOf("11.0.1"),
	fullVersion = "11.0.1+13-LTS",
	cleanVersion = "11.0.1",
	versionMajor = 11,
	versionUpdate = 1,
	downloadPageUrl = "/technetwork/java/javase/downloads/jdk11-downloads-5066655.html",
	downloadArchiveUrlPart = "http://download.oracle.com/otn-pub/java/jdk/11.0.1+13/90cf5d8f270a4347a95050320eef3fb7/jdk-11.0.1_"
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
	downloadArchiveUrlPart = "http://download.oracle.com/otn/java/jdk/11+28/55eed80b163941c8885ad9298e6d786a/jdk-11_"
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
	downloadArchiveUrlPart = "http://download.oracle.com/otn/java/jdk/10.0.2+13/19aef61b38124481863b1413dce1855f/jdk-10.0.2_"
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
	downloadArchiveUrlPart = "http://download.oracle.com/otn/java/jdk/10.0.1+10/fb4372174a714e6b8c52526dc134031e/jdk-10.0.1_"
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
	downloadArchiveUrlPart = "http://download.oracle.com/otn/java/jdk/8u171-b11/512cd62ec5174c3487ac17c61aaa89e8/jdk-8u171-"
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
	downloadArchiveUrlPart = "http://download.oracle.com/otn/java/jdk/8u172-b11/a58eab1ec242421181065cdc37240b08/jdk-8u172-"
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
	downloadArchiveUrlPart = "http://download.oracle.com/otn-pub/java/jdk/8u191-b12/2787e4a523244c269598db4e85c51e0c/jdk-8u191-"
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