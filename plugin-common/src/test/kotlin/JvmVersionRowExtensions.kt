import io.kotlintest.tables.Row1
import io.kotlintest.tables.row
import samples.JvmVersionSample

internal fun List<JvmVersionSample?>.asRows(): Array<Row1<JvmVersionRow>> {
	return this.filterNotNull()
		.flatMap { (vendor, type, versions, fullVersion, cleanVersion, versionMajor, versionUpdate, downloadPageUrl, downloadArchiveUrlPart) ->
			versions.map {
				row(
					JvmVersionRow(
						vendor, type, it, fullVersion, cleanVersion, versionMajor, versionUpdate, downloadPageUrl, downloadArchiveUrlPart
					)
				)
			}
		}.toTypedArray()
}

data class JvmVersionRow(
	val vendor: String,
	val type: String,
	val version: String,
	val fullVersion: String,
	val cleanVersion: String,
	val versionMajor: Int,
	val versionUpdate: Int?,
	val downloadPageUrl: String,
	val downloadArchiveUrlPart: String
)
