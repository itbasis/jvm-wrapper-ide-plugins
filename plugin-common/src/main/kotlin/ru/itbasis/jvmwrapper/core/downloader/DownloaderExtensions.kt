package ru.itbasis.jvmwrapper.core.downloader

import ru.itbasis.jvmwrapper.core.jvm.Jvm

fun Jvm.downloader(): AbstractDownloader {
	return DownloaderFactory.getInstance(this)
}