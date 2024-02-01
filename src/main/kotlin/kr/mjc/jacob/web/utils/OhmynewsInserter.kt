package kr.mjc.jacob.web.utils

import com.google.gson.Gson
import kr.mjc.jacob.web.dao.Post
import kr.mjc.jacob.web.dao.PostDao
import org.json.XML
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import java.io.InputStreamReader
import java.net.URI
import java.util.*
import javax.sql.DataSource

class OhmynewsInserter {

  data class News(var rss: Rss) {
    data class Rss(var channel: Channel)
    data class Channel(var item: List<Item>)
    data class Item(var title: String, var description: String,
        var pubDate: String) {
      fun toPost() =
        Post(title = this.title, content = this.description, userId = 1,
            name = "Jacob")
    }
  }

  fun run() {
    val ohmynews = URI.create("https://rss.ohmynews.com/rss/top.xml").toURL()
    val jsonObj = XML.toJSONObject(InputStreamReader(ohmynews.openStream()))
    val jsonStr = jsonObj.toString().replace("<br>", "").replace("&nbsp;", " ")
    val news = Gson().fromJson(jsonStr, News::class.java)

    val postDao = PostDao(NamedParameterJdbcTemplate(getDataSource()))
    news.rss.channel.item.forEach { item -> postDao.addPost(item.toPost()) }
  }

  private fun getDataSource(): DataSource {
    val props = Properties()
    props.load(OhmynewsInserter::class.java.getResourceAsStream(
        "/application.properties"))
    val url = props.getProperty("spring.datasource.url")
    return DriverManagerDataSource(url)
  }
}

fun main() {
  OhmynewsInserter().run()
}

