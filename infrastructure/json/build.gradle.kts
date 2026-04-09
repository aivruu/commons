plugins {
  id("commons.publish-conventions")
}

dependencies {
  api(project(":${rootProject.name}-api"))

  compileOnly(libs.paper.api) // use paper's shaded GSON version
}