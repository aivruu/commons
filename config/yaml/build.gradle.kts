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
  val dependencyMap = mapOf("name" to "configurate-yaml-$forkVersion", "ext" to "jar")
  implementation(dependencyMap)
  testRuntimeOnly(dependencyMap)
}