package kr.mjc.jacob.web.servlets

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.mjc.jacob.web.repository.User
import kr.mjc.jacob.web.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.thymeleaf.context.Context

@Controller
class ExampleRequestHandler(val userRepository: UserRepository) {

  fun hello(req: HttpServletRequest, resp: HttpServletResponse,
            context: Context) {
    context.setVariable("name", "Jacob")
  }

  fun users(req: HttpServletRequest, resp: HttpServletResponse,
            context: Context) {
    val userPage: Page<User> =
      userRepository.findAll(PageRequest.of(0, 10, Sort.Direction.DESC, "id"))
    context.setVariable("page", userPage)
  }
}