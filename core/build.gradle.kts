plugins {
  id("commons.publish-conventions")
}

dependencies {
  api(project(":${rootProject.name}-api"))

  compileOnlyApi(libs.paper.api)
  compileOnly(libs.configurate)
}