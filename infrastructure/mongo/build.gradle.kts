plugins {
  id("commons.shadow-conventions")
}

dependencies {
  api(project(":${rootProject.name}-api"))

  compileOnlyApi(libs.mongodb.driver)
}