package kr.mjc.jacob.web

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class WebSecurityConfig {

  private val log = LoggerFactory.getLogger(this::class.java)

  @Bean
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    return http.authorizeHttpRequests { requests ->
      requests.requestMatchers("/post/post_create", "/post/post_update",
          "/post/deletePost", "/user/profile", "/user/password").authenticated()
      requests.anyRequest().permitAll()
    }.formLogin { form ->
      form.loginPage("/login")
      form.defaultSuccessUrl("/user/user_list")
    }.logout { logout -> logout.logoutSuccessUrl("/user/user_list") }.build()
  }

  @Bean
  fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
