@file:Suppress("MatchingDeclarationName")

package samples

import ru.itbasis.jvmwrapper.core.downloader.RemoteArchiveFile

data class JvmVersionSample(
	val vendor: String,
	val type: String,
	val versions: List<String>,
	val fullVersion: String,
	val cleanVersion: String,
	val versionMajor: Int,
	val versionUpdate: Int?,
	val versionEarlyAccess: Boolean = false,
	val downloadPageUrl: String,
	val downloadArchiveUrlPart: String,
	val remoteFiles: Map<String, RemoteArchiveFile> = emptyMap()
)

val JvmVersionLatestSamples = listOf(
	OracleJvmVersionLatestSamples, OpenJDKJvmVersionLatestSamples
).flatten()

val JvmVersionArchiveSamples = listOf(
	OracleJvmVersionArchiveSamples
).flatten()

val JvmVersionEarlyAccessSamples = listOf(
	OpenJDKJvmVersionEarlyAccessSamples
).flatten()

val JvmVersionSamples = listOf(
	JvmVersionLatestSamples, JvmVersionEarlyAccessSamples, JvmVersionArchiveSamples
).flatten()
