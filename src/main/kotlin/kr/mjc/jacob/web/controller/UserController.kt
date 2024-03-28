package kr.mjc.jacob.web.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpSession
import kr.mjc.jacob.web.repository.User
import kr.mjc.jacob.web.repository.UserRepository
import kr.mjc.jacob.web.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.support.RedirectAttributes

/**
 * Servlet API를 사용하지 않는 컨트롤러
 */
@Controller
class UserController(val userRepository: UserRepository,
                     val passwordEncoder: PasswordEncoder,
                     val userService: UserService) {

  companion object {
    const val LANDING_PAGE = "redirect:/user/user_list"
    const val PAGE_SIZE = 10
  }

  private val log = LoggerFactory.getLogger(this::class.java)

  /**
   * 회원목록
   */
  @GetMapping("/user/user_list")
  fun userList(page: Int = 0, model: Model) {
    val userPage: Page<User> = userRepository.findAll(
        PageRequest.of(page, PAGE_SIZE, Sort.Direction.DESC, "id"))
    model.addAttribute("page", userPage)
  }

  /**
   * 회원가입
   */
  @PostMapping("/signup")
  fun signup(user: User, session: HttpSession,
             attributes: RedirectAttributes): String {
    val exists = userRepository.existsByUsername(user.username)
    if (!exists) {   // 이메일이 없음. 등록 진행
      user.password = passwordEncoder.encode(user.password)
      userRepository.save(user) // 등록 성공
      userService.login(user, session)   // 회원가입 후 자동 로그인
      return LANDING_PAGE
    } else {  // 이메일 존재. 등록 실패
      attributes.addFlashAttribute("duplicate_email", "duplicate_email")
      return "redirect:/signup"
    }
  }

  /**
   * 비밀번호변경
   */
  @PostMapping("/user/password")
  fun password(@AuthenticationPrincipal user: User, oldPassword: String,
               newPassword: String, attributes: RedirectAttributes): String {
    return if (passwordEncoder.matches(oldPassword, user.password)) { // 비밀번호 매치
      val hashedPassword = passwordEncoder.encode(newPassword)
      userRepository.changePassword(user.id, hashedPassword) // DB의 비밀번호 변경
      user.password = hashedPassword  // 세션의 비밀번호 변경
      attributes.addFlashAttribute("password_changed", "password_changed")
      "redirect:/user/profile"
    } else {  // 비밀번호 매치 안함
      attributes.addFlashAttribute("wrong_password", "wrong_password")
      "redirect:/user/password"
    }
  }

  /**
   * 회원정보
   */
  @GetMapping("/user/user_detail")
  fun userDetail(id: Int, model: Model) {
    try {
      model.addAttribute("user", userRepository.findById(id).orElseThrow())
    } catch (e: Exception) {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST) // 400
    }
  }

  /**
   * just forward
   */
  @GetMapping("/{_:post|user}/**", "/login", "/signup", "/hello", "/examples")
  fun pass(req: HttpServletRequest) {
    log.debug("servletPath = {}", req.servletPath)
  }
}
