plugins {
  id("commons.shadow-conventions")
  id("commons.test-conventions")
}

dependencies {
  api(project(":${rootProject.name}-api"))

  // As we're using a fork, we need to exclude the original core-module.
  compileOnlyApi(libs.configurate) {
    exclude(group = "org.spongepowered", module = "configurate-core")
  }
  includeLocalLibs()
}

fun DependencyHandlerScope.includeLocalLibs() {
  val forkVersion = "4.3.1-SNAPSHOT"

  listOf("configurate-core", "configurate-gson", "configurate-yaml").forEach {
    compileOnlyApi(":$it-$forkVersion")
    testRuntimeOnly(":$it-$forkVersion")
  }

  // Required by configurate-core
  compileOnly(libs.geantyref)
  testRuntimeOnly(libs.geantyref)
}

repositories {
  flatDir {
    dirs("libs") // import local libs
  }
}
