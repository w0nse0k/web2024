package kr.mjc.jacob.web.repository

import kr.mjc.jacob.web.formatted
import org.owasp.encoder.Encode
import org.springframework.data.annotation.Id
import java.time.LocalDateTime

data class Post(@Id val id: Int = 0, var title: String = "",
                var content: String = "", var userId: Int = 0,
                var firstName: String = "",
                val pubDate: LocalDateTime = LocalDateTime.now(),
                var lastModified: LocalDateTime = LocalDateTime.now()) {

  fun setUser(user: User) = this.apply {
    userId = user.id
    firstName = user.firstName
  }

  val contentBr get() = content.replace("\n", "<br/>\n")

  val contentHtml get() = Encode.forHtml(content).replace("\n", "<br/>\n")

  val pubDateFormatted get() = pubDate.formatted

  val lastModifiedFormatted get() = lastModified.formatted
}