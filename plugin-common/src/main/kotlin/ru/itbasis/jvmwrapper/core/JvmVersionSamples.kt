@file:Suppress("MatchingDeclarationName")

package ru.itbasis.jvmwrapper.core

data class JvmVersionSample(
  val vendor: String,
  val type: String,
  val versions: List<String>,
  val fullVersion: String,
  val cleanVersion: String,
  val versionMajor: String,
  val versionUpdate: String?,
  val downloadPageUrl: String,
  val downloadArchiveUrlPart: String
)

val jvmVersionSample__oracle_jdk_10_0_1 = JvmVersionSample(
  vendor = "oracle",
  type = "jdk",
  versions = listOf("10.0.1"),
  fullVersion = "10.0.1+10",
  cleanVersion = "10.0.1",
  versionMajor = "10",
  versionUpdate = null,
  downloadPageUrl = "/technetwork/java/javase/downloads/jdk10-downloads-4416644.html",
  downloadArchiveUrlPart = "http://download.oracle.com/otn-pub/java/jdk/10.0.1+10/fb4372174a714e6b8c52526dc134031e/jdk-10.0.1_"
)
val jvmVersionSample__oracle_jdk_10_0_2 = JvmVersionSample(
  vendor = "oracle",
  type = "jdk",
  versions = listOf("10.0.2"),
  fullVersion = "10.0.2+13",
  cleanVersion = "10.0.2",
  versionMajor = "10",
  versionUpdate = null,
  downloadPageUrl = "/technetwork/java/javase/downloads/jdk10-downloads-4416644.html",
  downloadArchiveUrlPart = "http://download.oracle.com/otn-pub/java/jdk/10.0.2+13/19aef61b38124481863b1413dce1855f/jdk-10.0.2"
)

val jvmVersionSample__oracle_jdk_8u171 = JvmVersionSample(
  vendor = "oracle",
  type = "jdk",
  versions = listOf("8u171", "1.8.0_171-b11"),
  fullVersion = "1.8.0_171-b11",
  cleanVersion = "8u171",
  versionMajor = "8",
  versionUpdate = "171",
  downloadPageUrl = "/technetwork/java/javase/downloads/jdk8-downloads-2133151.html",
  downloadArchiveUrlPart = "http://download.oracle.com/otn-pub/java/jdk/8u171-b11/512cd62ec5174c3487ac17c61aaa89e8/jdk-8u171-"
)
val jvmVersionSample__oracle_jdk_8u172 = JvmVersionSample(
  vendor = "oracle",
  type = "jdk",
  versions = listOf("8u172", "1.8.0_172-b11"),
  fullVersion = "1.8.0_172-b11",
  cleanVersion = "8u172",
  versionMajor = "8",
  versionUpdate = "172",
  downloadPageUrl = "/technetwork/java/javase/downloads/jdk8-downloads-2133151.html",
  downloadArchiveUrlPart = "http://download.oracle.com/otn-pub/java/jdk/8u172-b11/a58eab1ec242421181065cdc37240b08/jdk-8u172-"
)

val jvmVersionSample__oracle_jdk_8u181 = JvmVersionSample(
  vendor = "oracle",
  type = "jdk",
  versions = listOf("8u181", "1.8.0_181-b13"),
  fullVersion = "1.8.0_181-b13",
  cleanVersion = "8u181",
  versionMajor = "8",
  versionUpdate = "181",
  downloadPageUrl = "/technetwork/java/javase/downloads/jdk8-downloads-2133151.html",
  downloadArchiveUrlPart = "http://download.oracle.com/otn-pub/java/jdk/8u181-b13/96a7b8442fe848ef90c96a2fad6ed6d1/jdk-8u181-"
)
val JvmVersionLatestSamples = arrayOf(
  jvmVersionSample__oracle_jdk_10_0_2, jvmVersionSample__oracle_jdk_8u181
)
