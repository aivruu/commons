plugins {
  id("commons.shadow-conventions")
  id("commons.test-conventions")
}

dependencies {
  api(project(":${rootProject.name}-api"))

  // As we're using a fork as of now, we need to exclude the original core-module.
  compileOnlyApi(libs.configurate) {
    exclude(group = "org.spongepowered", module = "configurate-core")
  }
  includeLocalLibs()
}

fun DependencyHandlerScope.includeLocalLibs() {
  val forkVersion = "4.2.0-SNAPSHOT"

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
