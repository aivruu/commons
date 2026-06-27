plugins {
  id("commons.config-conventions")
}

dependencies {
  api(project(":${rootProject.name}-config"))

  val forkVersion: String = libs.versions.configurate.fork.get()
//  implementation(":configurate-toml-$forkVersion")
//  testRuntimeOnly(":configurate-toml-$forkVersion")
}