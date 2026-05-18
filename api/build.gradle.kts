plugins {
  id("commons.publish-conventions")
  id("commons.test-conventions")
}

dependencies {
  compileOnlyApi(libs.paper.api)
}