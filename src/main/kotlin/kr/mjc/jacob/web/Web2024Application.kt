package kr.mjc.jacob.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication
@ServletComponentScan(basePackages = ["kr.mjc.jacob.web.servlets"])
class Web2024Application : SpringBootServletInitializer() {
  /**
   * war 파일을 위한 구성
   */
  override fun configure(
      application: SpringApplicationBuilder): SpringApplicationBuilder {
    return application.sources(Web2024Application::class.java)
  }
}

fun main(args: Array<String>) {
  runApplication<Web2024Application>(*args)
}
