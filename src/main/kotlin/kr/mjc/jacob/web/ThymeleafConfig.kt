package kr.mjc.jacob.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.TemplateEngine
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

@Configuration
class ThymeleafConfig {

  @Bean
  fun thymeleaf(): TemplateEngine {
    val templateResolver = ClassLoaderTemplateResolver().apply {
      prefix = "/templates/"
      suffix = ".html"
    }
    return TemplateEngine().apply {
      setTemplateResolver(templateResolver)
    }
  }
}