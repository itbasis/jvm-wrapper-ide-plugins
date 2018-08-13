package ru.itbasis.jvmwrapper.core.vendor

import ru.itbasis.jvmwrapper.core.HttpClient

abstract class AbstractAuthenticator(protected val httpClient: HttpClient) {
  abstract fun execute(): Boolean
  abstract val username: String
  abstract val password: String
}
