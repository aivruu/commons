plugins {
  id("commons.config-conventions")
}

repositories {
  flatDir {
    dirs("libs") // import local libs
  }
}

dependencies {
  api(project(":${rootProject.name}-config"))

  val forkVersion: String = libs.versions.configurate.fork.get()
//  implementation(":configurate-toml-$forkVersion")
//  testRuntimeOnly(":configurate-toml-$forkVersion")
}