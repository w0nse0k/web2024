package kr.mjc.jacob.web.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.mjc.jacob.web.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

@WebServlet("/servlets/users")
class UserListServlet : HttpServlet() {

  /* servlet은 constructor를 사용하지 않으므로 constructor inject을 하지 않고
   * @AutowiredWired를 사용해서 field injection을 한다.
   * servlet을 생성한 후에 injection을 하므로 lateinit var로 설정한다.*/
  @Autowired lateinit var userRepository: UserRepository

  override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    val users =
      userRepository.findAll(PageRequest.of(0, 10, Sort.Direction.DESC, "id"))
    val builder = StringBuilder()
    users.forEach { user ->
      builder.append(
          "<div>${user.id}, ${user.firstName}, ${user.username}</div>")
    }

    val html = """
        <!DOCTYPE html>
        <html>
        <body>
        <h3>회원목록</h3>
        $builder
        </body>
        </html>
        """.trimIndent()

    resp.contentType = "text/html"
    resp.writer.println(html)
  }
}