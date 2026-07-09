plugins {
  id("commons.shadow-conventions")
  id("commons.test-conventions")
}

dependencies {
  api(project(":${rootProject.name}-core"))

  compileOnlyApi(libs.expirable.cache) // aka Caffeine
  testRuntimeOnly(libs.expirable.cache)
}