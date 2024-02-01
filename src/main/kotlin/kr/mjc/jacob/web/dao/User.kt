package kr.mjc.jacob.web.dao

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * 세션에 넣는 사용자 정보. 세션에 넣는 객체는 Serializable을 구현해야
 * 웹서버를 재시작할 때 세션에 다시 올라간다.
 */
data class User(var userId: Int = 0, var email: String = "",
    @JvmField var password: String = "", var name: String = "") : UserDetails {

  fun setPassword(value: String) {
    this.password = value
  }

  @JvmField var authorities = setOf<GrantedAuthority>()

  override fun getUsername() = email
  override fun getAuthorities() = authorities
  override fun getPassword() = password
  override fun isAccountNonExpired() = true
  override fun isAccountNonLocked() = true
  override fun isCredentialsNonExpired() = true
  override fun isEnabled() = true

  override fun toString(): String {
    return "User(userId=$userId, email='$email', name='$name', username='$username', authorities=$authorities)"
  }
}