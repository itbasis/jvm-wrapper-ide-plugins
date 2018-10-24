package ru.itbasis.jvmwrapper.core.downloader

import ru.itbasis.jvmwrapper.core.FileNameExtension

data class RemoteArchiveFile(val url: String, val checksum: String? = null, val archiveFileExtension: FileNameExtension)
