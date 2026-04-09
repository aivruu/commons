plugins {
  id("commons.publish-conventions")
}

dependencies {
  api(project(":${rootProject.name}-api"))

  compileOnly(libs.mongodb.driver)
}