package kr.mjc.jacob.web.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpSession
import kr.mjc.jacob.web.dao.Limit
import kr.mjc.jacob.web.dao.User
import kr.mjc.jacob.web.dao.UserDao
import kr.mjc.jacob.web.user
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

/**
 * Servlet API를 사용하지 않는 컨트롤러
 */
@Controller
class UserController(val userDao: UserDao,
    val passwordEncoder: PasswordEncoder) {

  private val log = LoggerFactory.getLogger(this::class.java)

  /**
   * 회원목록
   */
  @GetMapping("/user/users")
  fun users(limit: Limit, model: Model, req: HttpServletRequest) {
    model.addAttribute("users", userDao.listUsers(limit))
  }

  /**
   * 회원가입 화면
   */
  @GetMapping("/signup")
  fun signupForm(session: HttpSession) {
    session.invalidate()
  }

  /**
   * 회원가입
   */
  @PostMapping("/signup")
  fun signup(user: User, session: HttpSession): String {
    try {
      user.password = passwordEncoder.encode(user.password)
      userDao.addUser(user) // 등록 성공
      // HttpSession property extension. 세션의 SecurityContext에 auth 넣음
      session.user = user
      return "redirect:/user/users"
    } catch (e: DataAccessException) { // 등록 실패
      log.error(e.cause.toString())
      return "redirect:/signup?error"
    }
  }

  /**
   * 비밀번호변경
   */
  @PostMapping("/user/password")
  fun password(@AuthenticationPrincipal user: User, password: String,
      newPassword: String): String {
    // matches(raw, encoded)
    if (passwordEncoder.matches(password, user.password)) {
      val encodedPassword = passwordEncoder.encode(newPassword)
      userDao.changePassword(user.userId, encodedPassword)
      user.password = encodedPassword
      return "redirect:/user/me"
    } else {
      return "redirect:/user/password?error"
    }
  }

  /**
   * 회원정보
   */
  @GetMapping("/user/user")
  fun user(userId: Int, model: Model) {
    model.addAttribute("user", userDao.getUser(userId))
  }

  /**
   * just forward
   */
  @GetMapping("/{_:post|user}/**", "/login")
  fun pass(req: HttpServletRequest) {
    log.debug("servletPath = {}", req.servletPath)
  }
}
