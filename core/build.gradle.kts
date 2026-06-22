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

  // Import local Configurate fork
  val forkVersion = "4.2.0-SNAPSHOT"
  api(":configurate-core-$forkVersion")
  api(":configurate-gson-$forkVersion")
  api(":configurate-yaml-$forkVersion")

  // Required by configurate-core
  compileOnly(libs.geantyref)
  testRuntimeOnly(libs.geantyref)

  testImplementation(":configurate-core-$forkVersion")
  testImplementation(":configurate-gson-$forkVersion")
  testImplementation(":configurate-yaml-$forkVersion")
}

repositories {
  flatDir {
    dirs("libs") // import local libs
  }
}
