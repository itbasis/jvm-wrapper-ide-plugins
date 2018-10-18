package ru.itbasis.jvmwrapper.core.wrapper

import ru.itbasis.jvmwrapper.core.downloader.RemoteArchiveFile
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import java.io.File
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter

class LastUpdateFile(jvm: Jvm) {
	val file: File by lazy {
		val fileName = jvm.toString().toLowerCase() + FILE_EXTENSION
		return@lazy File(JVMW_HOME_DIR, fileName)
	}

	private var updated: LocalDateTime = now().minusDays(1)
	private var archiveRemoteUrl: String? = null
	private var archiveChecksum: String? = null

	init {
		read()
	}

	fun isExpired(): Boolean {
		read()
		val today = now()
		return Duration.between(updated, today).toDays() > 0
	}

	fun equals(remoteArchiveFile: RemoteArchiveFile): Boolean {
		read()
		if (remoteArchiveFile.url == archiveRemoteUrl) {
			return true
		}
		if (remoteArchiveFile.checksum == archiveChecksum) {
			return true
		}
		return false
	}

	private fun read() {
		file.takeIf {
			it.isFile
		}?.useLines {
			it.forEachIndexed { index, row ->
				when (index) {
					0    -> updated = LocalDateTime.parse(row, DATE_TIME_FORMATTER)
					1    -> archiveRemoteUrl = row
					2    -> archiveChecksum = row

					else -> throw IllegalStateException("unsupported line count in file: $file")
				}
			}
		}
	}

	fun update(remoteArchiveFile: RemoteArchiveFile? = null) {
		updated = now()
		val text = arrayOf(
			updated.format(DATE_TIME_FORMATTER),
			remoteArchiveFile?.url
			?: archiveRemoteUrl
			?: "",
			remoteArchiveFile?.checksum
			?: archiveChecksum
			?: ""
		).joinToString("\n")
		file.writeText(text)
	}

	companion object {
		@JvmStatic
		val FILE_EXTENSION = ".last_update"

		@JvmStatic
		private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
	}
}
