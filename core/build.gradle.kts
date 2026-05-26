plugins {
  id("commons.publish-conventions")
  id("commons.test-conventions")
}

dependencies {
  api(project(":${rootProject.name}-api"))

  compileOnlyApi(libs.paper.api)
  compileOnlyApi(libs.configurate)
  compileOnlyApi(libs.configurate.yaml)
  compileOnlyApi(libs.configurate.json)

  testRuntimeOnly(libs.configurate.yaml)
  testRuntimeOnly(libs.configurate.json)
}