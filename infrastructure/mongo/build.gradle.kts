plugins {
  id("commons.shadow-conventions")
}

dependencies {
  api(project(":${rootProject.name}-core"))

  compileOnlyApi(libs.mongodb.driver)
}