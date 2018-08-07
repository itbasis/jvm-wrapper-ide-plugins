package ru.itbasis.jvmwrapper.core

typealias ProcessStepListener = (msg: String) -> Unit

inline fun <R> String.step(noinline stepListener: ProcessStepListener? = null, command: () -> R): R {
  stepListener?.invoke(this)
  return command()
}
