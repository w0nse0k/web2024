package kr.mjc.jacob.web.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@WebServlet("/dispatcher/*")
class DispatcherServlet : HttpServlet() {

  @Autowired lateinit var exampleHandler: ExampleRequestHandler
  @Autowired lateinit var thymeleaf: TemplateEngine

  override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    println("servletPath = ${req.servletPath}")
    println("pathInfo = ${req.pathInfo}")
    val context = Context()
    when (req.pathInfo) {
      "/examples/hello" -> exampleHandler.hello(req, resp, context)
      "/examples/users" -> exampleHandler.users(req, resp, context)
    }
    val result = thymeleaf.process(req.pathInfo, context)
    resp.contentType = "text/html"
    resp.writer.println(result)
  }
}

fun Context.set(name: String, value: Any): Context {
  this.setVariable(name, value)
  return this
}
