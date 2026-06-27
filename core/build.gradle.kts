plugins {
  id("commons.shadow-conventions")
  id("commons.test-conventions")
}

dependencies {
  api(project(":${rootProject.name}-api"))
}
