package kr.mjc.jacob.web.repository

import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.transaction.annotation.Transactional

/**
 * SpringDataJdbc를 사용한 user repository
 * Repository 인터페이스를 확장한 인터페이스의 구현체는
 * 개발자가 구현하지 않고 스프링이 구현한다.
 * @author Jacob
 */
interface UserRepository : CrudRepository<User, Int>,
    PagingAndSortingRepository<User, Int> {

  fun findByUsername(username: String): User?

  fun existsByUsername(username: String): Boolean

  @Modifying
  @Transactional
  @Query("update user set password=:password where id=:id")
  fun changePassword(id: Int, password: String)
}
