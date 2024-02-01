package kr.mjc.jacob.web.dao

import kr.mjc.jacob.web.toMap
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

/**
 * JdbcTemplate과 NamedParameterJdbcTemplate을 사용한 User Data Access Object
 *
 * @author Jacob
 */
@Repository
class UserDao(val jt: NamedParameterJdbcTemplate) {

  companion object {
    const val LIST_USERS =
      "select userId, email, name from user order by userId desc limit :offset, :count"

    const val GET_USER =
      "select userId, email, password, name from user where userId=:userId"

    const val ADD_USER =
      "insert user(email, password, name) values(:email,:password,:name) returning userId"

    const val CHANGE_PASSWORD =
      "update user set password=:password where userId=:userId"

    const val DELETE_USER =
      "delete from user where userId=:userId and password=:password"
  }

  private val log = LoggerFactory.getLogger(this::class.java)

  /**
   * resultSet을 user에 자동 매핑하는 매퍼
   */
  private val userMapper: RowMapper<User> =
    BeanPropertyRowMapper(User::class.java)

  /**
   * 회원 목록
   * @param limit 목록의 갯수와 페이지
   * @return 회원 목록
   */
  fun listUsers(limit: Limit): List<User> {
    return jt.query(LIST_USERS, limit.toMap(), userMapper)
  }

  /**
   * 회원정보 조회
   * @param userId 회원번호
   * @return 회원 정보
   */
  fun getUser(userId: Int): User? {
    val params = mapOf("userId" to userId)
    return jt.queryForObject(GET_USER, params, userMapper)
  }

  /**
   * 회원 가입
   * @param user 회원정보
   * @throws DataAccessException 이메일 중복으로 회원가입 실패 시
   */
  fun addUser(user: User) {
    user.userId = jt.queryForObject(ADD_USER, user.toMap(), Int::class.java)!!
  }

  /**
   * 비밀번호 수정
   *
   * @param userId    회원번호
   * @param password  새 비밀번호
   * @return 수정 성공시 1, 회원이 없거나 비밀번호가 틀리면 0
   */
  fun changePassword(userId: Int, password: String): Int {
    val params = mapOf("userId" to userId, "password" to password)
    return jt.update(CHANGE_PASSWORD, params)
  }

  /**
   * 회원 삭제
   *
   * @param userId   회원번호
   * @param password 비밀번호
   * @return 삭제 성공시 1, 회원번호가 없거나 비밀번호가 틀리면 0
   */
  fun deleteUser(userId: Int, password: String): Int {
    val params = mapOf("userId" to userId, "password" to password)
    return jt.update(DELETE_USER, params)
  }
}
