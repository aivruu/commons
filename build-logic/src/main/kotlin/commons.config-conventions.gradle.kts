import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar.Companion.shadowJar
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.getByType

plugins {
  id("commons.shadow-conventions")
  id("commons.test-conventions")
}

val libs = extensions.getByType(LibrariesForLibs::class)

dependencies {
  val forkVersion: String = libs.versions.configurate.fork.get()
  // Assuming is available in the 'libs' dir of each subproject of 'config'.
  implementation(":configurate-core-$forkVersion")
  testImplementation(":configurate-core-$forkVersion")

  // Required by configurate-core
  compileOnly(libs.geantyref)
  testImplementation(libs.geantyref)
}

tasks {
  shadowJar {
    val route = "${findProperty("group") as String}.library.infrastructure.org.spongepowered.configurate"
    relocate("org.spongepowered.configurate", route)
  }
}
