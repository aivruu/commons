import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar.Companion.shadowJar
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.getByType

plugins {
  id("commons.shadow-conventions")
  id("commons.test-conventions")
}

val libs = extensions.getByType(LibrariesForLibs::class)

tasks.withType(GenerateModuleMetadata::class.java).configureEach {
  suppressedValidationErrors.add("dependencies-without-versions")
}

dependencies {
  val forkVersion: String = libs.versions.configurate.fork.get()
  val dependencyMap = mapOf("name" to "configurate-core-$forkVersion", "ext" to "jar")
  // Assuming is available in the 'libs' dir of each subproject of 'config'.
  implementation(dependencyMap)
  testImplementation(dependencyMap)

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
