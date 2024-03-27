package kr.mjc.jacob.web

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpSession
import kr.mjc.jacob.web.repository.User
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import java.net.URLEncoder
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT"

/**
 * queryString을 포함한 full url
 */
val HttpServletRequest.fullUrl: String
  get() = if (queryString == null) requestURL.toString() else "$requestURL?$queryString"

/**
 * Spring Security User
 */
var HttpSession.user: User?
  get() = (this.getAttribute(
      SPRING_SECURITY_CONTEXT) as? SecurityContext)?.authentication?.principal as? User
  set(value) {
    this.setAttribute(SPRING_SECURITY_CONTEXT, SecurityContextImpl(
        UsernamePasswordAuthenticationToken(value, null, value!!.authorities)))
  }

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
