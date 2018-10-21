package samples

import io.kotlintest.tables.Row1
import io.kotlintest.tables.row
import ru.itbasis.jvmwrapper.core.downloader.RemoteArchiveFile

data class JvmVersionRow(
	val vendor: String,
	val type: String,
	val version: String,
	val fullVersion: String,
	val cleanVersion: String,
	val versionMajor: Int,
	val versionUpdate: Int?,
	val versionEarlyAccess: Boolean,
	val downloadPageUrl: String,
	val downloadArchiveUrlPart: String,
	val remoteFiles: Map<String, RemoteArchiveFile> = emptyMap()
)

internal fun List<JvmVersionSample?>.asKotlinTestRows(): Array<Row1<JvmVersionRow>> {
	return this.filterNotNull().flatMap { jvmVersionSample ->
		jvmVersionSample.asJvmVersionRow()
	}.map { jvmVersionRow ->
		row(jvmVersionRow)
	}.toTypedArray()
}

internal fun JvmVersionSample.asJvmVersionRow(): List<JvmVersionRow> {
	return versions.map { version ->
		JvmVersionRow(
			vendor = vendor,
			type = type,
			version = version,
			fullVersion = fullVersion,
			cleanVersion = cleanVersion,
			versionMajor = versionMajor,
			versionUpdate = versionUpdate,
			versionEarlyAccess = versionEarlyAccess,
			downloadPageUrl = downloadPageUrl,
			downloadArchiveUrlPart = downloadArchiveUrlPart,
			remoteFiles = remoteFiles
		)
	}
}
