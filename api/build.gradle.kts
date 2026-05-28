plugins {
  id("commons.publish-conventions")
  id("commons.test-conventions")
}

dependencies {
  testImplementation(libs.paper.api)
}