plugins {
  id("commons.config-conventions")
}

dependencies {
  api(project(":${rootProject.name}-config"))

  val forkVersion: String = libs.versions.configurate.fork.get()
  implementation(":configurate-yaml-$forkVersion")
  testRuntimeOnly(":configurate-yaml-$forkVersion")
}