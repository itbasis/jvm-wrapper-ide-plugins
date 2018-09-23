package ru.itbasis.jvmwrapper.core.downloader.oracle

import ru.itbasis.jvmwrapper.core.HttpClient
import ru.itbasis.jvmwrapper.core.findOne
import ru.itbasis.jvmwrapper.core.downloader.AbstractAuthenticator

class OracleAuthenticator(httpClient: HttpClient) : AbstractAuthenticator(httpClient) {
  companion object {
    private const val OTN_URL_INDEX = "https://www.oracle.com/index.html"
    private const val OTN_HOST_LOGIN = "https://login.oracle.com"
    private const val OTN_URL_SIGNON = "http://www.oracle.com/webapps/redirect/signon?nexturl=$OTN_URL_INDEX"
    private const val OTN_URL_SIGNOUT = "https://login.oracle.com/sso/logout?p_done_url=$OTN_URL_INDEX"

    private val FORM_REGEX = "(?is)(<form.+?>.*?</form>)".toRegex(option = RegexOption.IGNORE_CASE)
    private val FORM_ACTION_REGEX = "action=\"([^\"]+)".toRegex(option = RegexOption.IGNORE_CASE)
    private val FORM_INPUTS_REGEX = "(<input.*?>)".toRegex(option = RegexOption.IGNORE_CASE)
    private val FORM_INPUT_NAME_REGEX = "name=\"(.*?)\"".toRegex(option = RegexOption.IGNORE_CASE)
    private val FORM_INPUT_VALUE_REGEX = "value=\"(.*?)\"".toRegex(option = RegexOption.IGNORE_CASE)
    private val META_REFRESH_REGEX = "http-equiv=\"refresh\".*URL=(.*)\"".toRegex(RegexOption.IGNORE_CASE)

    const val JVMW_ORACLE_KEYCHAIN = "JVM_WRAPPER_ORACLE"
  }

  override val username: String by lazy {
    System.getenv("ORACLE_USER")
  }

  override val password: String by lazy {
    System.getenv("ORACLE_PASSWORD")
  }

  private var content = ""

  override fun execute(): Boolean {
    otnGetRequest(OTN_URL_INDEX)
    httpClient.httpCookieStore.cookies.find { it.name == "ORA_UCM_INFO" }?.let { cookie ->
      if (cookie.value.endsWith(suffix = username, ignoreCase = true)) {
        return true
      }
      otnGetRequest(OTN_URL_SIGNOUT)
      httpClient.httpCookieStore.clear()
    }


    otnGetRequest(OTN_URL_SIGNON)
    otnPostRequest()
    otnPostRequest()
    otnRedirectRequest()
    otnPostRequest()
    otnGetRequest(OTN_URL_SIGNON)

    // TODO check auth success
    return true
  }

  private fun otnRedirectRequest() {
    content = httpClient.getContent(META_REFRESH_REGEX.findOne(content)!!.fixUrl())
  }

  private fun otnGetRequest(url: String) {
    content = httpClient.getContent(url)
  }

  private fun otnPostRequest(): Boolean {
    val formParser = formParser() ?: return false
    content = httpClient.post(formParser.first, formParser.second)
    return true
  }

  private fun formParser(): Pair<String, Map<String, String>>? {
    val form = FORM_REGEX.findOne(content) ?: return null
    val formAction: String = FORM_ACTION_REGEX.findOne(form)!!.fixUrl()

    val f = FORM_INPUTS_REGEX.findAll(form)
    val fields = f.mapNotNull {
      with(it.groupValues[1]) {
        val fieldName = FORM_INPUT_NAME_REGEX.findOne(this) ?: ""
        val fieldValue = when (fieldName.toLowerCase()) {
          "userid" -> username
          "pass" -> password
          else -> FORM_INPUT_VALUE_REGEX.findOne(this) ?: ""
        }
        fieldName to fieldValue
      }
    }.toMap()


    return formAction to fields
  }

  private fun String.fixUrl(): String {
    if (startsWith("/")) return OTN_HOST_LOGIN.plus(this)
    return this
  }
}