package ru.itbasis.jvmwrapper.core.wrapper

import ru.itbasis.jvmwrapper.core.downloader.RemoteArchiveFile
import java.io.File
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter

class LastUpdateFile(jvmName: String) {
	private val updatedFile = File(JVMW_HOME_DIR, "$jvmName.last_update")

	private var updated: LocalDateTime = now().minusDays(1)
	private var archiveRemoteUrl: String? = null
	private var archiveChecksum: String? = null

	init {
		read()
	}

	private fun read() {
		updatedFile.takeIf { it.isFile }?.useLines {
			it.forEachIndexed { index, row ->
				when (index) {
					0    -> updated = LocalDateTime.parse(row, DATE_TIME_FORMATTER)
					1    -> archiveRemoteUrl = row
					2    -> archiveChecksum = row

					else -> throw IllegalStateException("unsupported line count in file: $updatedFile")
				}
			}
		}
	}

	fun update(remoteArchiveFile: RemoteArchiveFile? = null) {
		updated = now()
		val text = arrayOf(
			updated.format(DATE_TIME_FORMATTER),
			remoteArchiveFile?.url ?: archiveRemoteUrl ?: "",
			remoteArchiveFile?.checksum ?: archiveChecksum ?: ""
		).joinToString("\n")
		updatedFile.writeText(text)
	}

	companion object {
		@JvmStatic
		private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
	}
}
