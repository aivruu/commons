plugins {
  id("commons.publish-conventions")
  id("commons.test-conventions")
}

dependencies {
  api(project(":${rootProject.name}-api"))

  compileOnly(libs.paper.api) // use paper's shaded fastutil version
  compileOnlyApi(libs.expirable.cache) // aka Caffeine, if required

  testImplementation(libs.paper.api)
  testRuntimeOnly(libs.expirable.cache)
}