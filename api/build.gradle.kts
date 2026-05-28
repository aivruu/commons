plugins {
  id("commons.publish-conventions")
  id("commons.test-conventions")
}

dependencies {
  compileOnly(libs.paper.api)
}