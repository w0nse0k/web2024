package kr.mjc.jacob.web

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpSession
import kr.mjc.jacob.web.dao.User
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

const val SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT"

/**
 * queryString을 포함한 full url
 */
val HttpServletRequest.fullUrl: String
  get() = if (queryString == null) requestURL.toString() else "$requestURL?$queryString"

/**
 * 오브젝트를 맵으로 변환
 */
fun Any.toMap(): Map<String, Any?> {
  return (this::class as KClass<Any>).memberProperties.associate { prop ->
    prop.name to prop.get(this)
  }
}

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
