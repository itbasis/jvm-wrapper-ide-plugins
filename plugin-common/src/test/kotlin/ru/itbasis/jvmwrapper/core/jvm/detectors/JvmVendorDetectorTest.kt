package ru.itbasis.jvmwrapper.core.jvm.detectors

import io.kotlintest.specs.FunSpec
import ru.itbasis.jvmwrapper.core.jvm.Jvm
import ru.itbasis.jvmwrapper.core.jvm.detectors.JvmVendorDetector.detect
import java.nio.file.Paths

//internal class JvmVendorDetectorTest : FunSpec() {
//	init {
//		test("detect") {
//			val path = Paths.get("/Library/Java/JavaVirtualMachines/jdk-11.jdk/Contents/Home")
//			println("path: $path")
//			val adjustPath = Jvm.adjustPath(path)
//			println("adjustPath: $adjustPath")
//			val detect = detect(adjustPath!!)
//			println(detect)
//		}
//	}
//}