package kr.mjc.jacob.web.service

import jakarta.servlet.http.HttpSession
import kr.mjc.jacob.web.repository.UserRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository) : UserDetailsService {

  companion object {
    const val SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT"
  }

  override fun loadUserByUsername(username: String): UserDetails {
    return userRepository.findByUsername(username)
      ?: throw UsernameNotFoundException(username)
  }

  fun login(user: UserDetails, session: HttpSession) {
    // Spring security context에 authentication 등록
    val authentication: Authentication =
      UsernamePasswordAuthenticationToken(user, null, user.authorities)
    session.setAttribute(SPRING_SECURITY_CONTEXT,
        SecurityContextImpl(authentication))
  }
}