import org.gradle.accessors.dm.LibrariesForLibs

plugins {
  id("commons.common-conventions")
}

val libs = extensions.getByType(LibrariesForLibs::class)

dependencies {
  testImplementation(libs.junit.api)
  testImplementation(libs.mockito.core)
  testImplementation(libs.mockito.junit.jupiter)

  testRuntimeOnly(libs.junit.engine)
  testRuntimeOnly(libs.junit.launcher)

  testRuntimeOnly(libs.paper.api)
}

tasks {
  test {
    useJUnitPlatform()
  }
}