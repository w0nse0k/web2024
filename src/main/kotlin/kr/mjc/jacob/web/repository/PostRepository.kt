package kr.mjc.jacob.web.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository

/**
 * SpringDataJdbc를 사용한 post repository
 * Repository 인터페이스를 확장한 인터페이스의 구현체는
 * 개발자가 구현하지 않고 스프링이 구현한다.
 * @author Jacob
 */
interface PostRepository : CrudRepository<Post, Int>,
    PagingAndSortingRepository<Post, Int>