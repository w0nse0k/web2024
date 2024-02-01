package kr.mjc.jacob.web.dao

import org.owasp.encoder.Encode

data class Post(var postId: Int = 0, var title: String = "",
    var content: String = "", var userId: Int = 0, var name: String = "",
    var cdate: String = "", var udate: String = "") {

  fun setUser(user: User) = this.apply {
    userId = user.userId
    name = user.name
  }

  val contentBr get() = content.replace("\n", "<br/>\n")

  val contentHtml get() = Encode.forHtml(content).replace("\n", "<br/>\n")
}