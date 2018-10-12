package ru.itbasis.jvmwrapper.core

import org.apache.commons.codec.digest.DigestUtils
import java.io.File

fun File.checksum256(checksum: String?): Boolean {
	if (checksum.isNullOrBlank()) {
		return true
	}
	return DigestUtils.sha256Hex(inputStream()) == checksum
}

internal fun Regex.findOne(content: String): String? {
	return find(content)?.groupValues?.get(1)
}
