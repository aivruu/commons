plugins {
  id("commons.publish-conventions")
  id("commons.test-conventions")
}

dependencies {
  api(project(":${rootProject.name}-api"))

  compileOnlyApi(libs.expirable.cache) // aka Caffeine, if required

  testImplementation(libs.paper.api)
  testRuntimeOnly(libs.expirable.cache)
}