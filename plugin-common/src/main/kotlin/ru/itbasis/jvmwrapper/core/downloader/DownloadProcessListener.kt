package ru.itbasis.jvmwrapper.core.downloader

typealias DownloadProcessListener = (remoteArchiveUrl: String, sizeCurrent: Long, sizeTotal: Long) -> Boolean

