import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  war
  id("org.springframework.boot") version "3.2.4"
  id("io.spring.dependency-management") version "1.1.4"
  kotlin("jvm") version "1.9.23"
  kotlin("plugin.spring") version "1.9.23"
}

group = "kr.mjc.jacob"
version = "0.0.1-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
  implementation("org.springframework.boot:spring-boot-starter-tomcat")
  developmentOnly("org.springframework.boot:spring-boot-devtools")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")

  // apache commons
  implementation("org.apache.commons:commons-lang3")

  // owasp java encoder
  implementation("org.owasp.encoder:encoder:1.+")
  implementation("org.owasp.encoder:encoder-jsp:1.+")

  // json
  implementation("org.json:json:+")
  implementation("com.google.code.gson:gson:+")

  // ognl
  runtimeOnly("ognl:ognl:+")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs += "-Xjsr305=strict"
    jvmTarget = "21"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
