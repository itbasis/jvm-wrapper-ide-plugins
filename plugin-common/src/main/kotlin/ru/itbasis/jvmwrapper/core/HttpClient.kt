package ru.itbasis.jvmwrapper.core

import mu.KotlinLogging
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.CookieStore
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.client.LaxRedirectStrategy
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.message.BasicNameValuePair
import org.apache.http.protocol.BasicHttpContext
import org.apache.http.protocol.HttpContext
import org.apache.http.util.EntityUtils

class HttpClient {
  private val logger = KotlinLogging.logger {}

  val httpCookieStore: CookieStore = BasicCookieStore()

  private val httpContext: HttpContext = BasicHttpContext().also {
    it.setAttribute(HttpClientContext.COOKIE_STORE, httpCookieStore)
  }
  private val requestConfig = RequestConfig.custom().setCircularRedirectsAllowed(true).build()

  private val client: CloseableHttpClient =
    HttpClients.custom().setUserAgent("Mozilla/5.0 https://github.com/itbasis/jvm-wrapper").setConnectionManager(
      PoolingHttpClientConnectionManager()
    ).setRedirectStrategy(LaxRedirectStrategy()).setDefaultRequestConfig(requestConfig).build()!!

  private fun HttpResponse.content(): String {
    return EntityUtils.toString(entity)
  }

  fun getContent(url: String): String {
    logger.debug { "get.url: $url" }
    return client.execute(HttpGet(url), httpContext).content()
  }

  fun getEntity(url: String): HttpEntity {
    logger.debug { "get.entity.url: $url" }
    val execute = client.execute(HttpGet(url), httpContext)
    return execute.entity
  }

  fun post(url: String, params: Map<String, String>): String {
    logger.debug { "post.url: $url" }
    val httpPost = HttpPost(url)
    httpPost.entity = UrlEncodedFormEntity(params.map { (key, value) -> BasicNameValuePair(key, value) })
    return client.execute(httpPost, httpContext).content()
  }
}
