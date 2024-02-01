package kr.mjc.jacob.web.service

import kr.mjc.jacob.web.dao.User
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserManager(val jt: NamedParameterJdbcTemplate) : UserDetailsService {
  companion object {
    const val DEF_USER_EXISTS_SQL =
      "select userId, email, password, name from user where email=:email"
  }

  private val log = LoggerFactory.getLogger(this::class.java)

  /**
   * resultSet을 user에 자동 매핑하는 매퍼
   */
  private val userMapper: RowMapper<User> =
    BeanPropertyRowMapper(User::class.java)

  override fun loadUserByUsername(username: String): UserDetails? {
    val params = mapOf("email" to username)
    try {
      return jt.queryForObject(DEF_USER_EXISTS_SQL, params, userMapper)
    } catch (e: DataAccessException) {
      log.error(e.toString())
      throw UsernameNotFoundException(username)
    }
  }
}