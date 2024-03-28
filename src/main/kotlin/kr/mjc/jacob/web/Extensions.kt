package kr.mjc.jacob.web

import jakarta.servlet.http.HttpServletRequest
import java.net.URLEncoder
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * queryString을 포함한 full url
 */
val HttpServletRequest.fullUrl: String
  get() = if (queryString == null) requestURL.toString() else "$requestURL?$queryString"

/**
 * url encoding
 */
val String.urlEncoded: String
  get() = URLEncoder.encode(this, Charset.defaultCharset())

/**
 * 날짜를 "yyyy-MM-dd HH:mm:ss"로 포맷하는 formatter
 */
val formatter: DateTimeFormatter =
  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

/**
 * 날짜를 formatter로 포맷한다.
 */
val LocalDateTime.formatted: String get() = this.format(formatter)
