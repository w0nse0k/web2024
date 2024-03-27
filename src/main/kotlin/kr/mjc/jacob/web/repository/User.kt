package kr.mjc.jacob.web.repository

import kr.mjc.jacob.web.formatted
import org.springframework.data.annotation.Id
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

/**
 * 세션에 넣는 사용자 정보. 세션에 넣는 객체는 Serializable을 구현해야
 * 웹서버를 재시작할 때 세션에 다시 올라간다.
 */
data class User(@Id val id: Int = 0, @JvmField val username: String = "",
                @JvmField var password: String = "", val firstName: String = "",
                val dateJoined: LocalDateTime = LocalDateTime.now()) :
    UserDetails {

  val dateJoinedFormatted: String get() = dateJoined.formatted

  override fun toString() =
    "User(id=$id, username='$username', firstName='$firstName', dateJoined=${
      dateJoined.formatted
    })"

  override fun getUsername() = username
  override fun getPassword() = password
  override fun getAuthorities() = setOf<GrantedAuthority>()

  override fun isAccountNonExpired() = true
  override fun isAccountNonLocked() = true
  override fun isCredentialsNonExpired() = true
  override fun isEnabled() = true
}
