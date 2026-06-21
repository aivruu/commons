import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar.Companion.shadowJar

plugins {
  id("commons.publish-conventions")
  id("com.gradleup.shadow")
}

val buildsDir: File = file("$rootDir/jars")

tasks {
  shadowJar {
    archiveFileName.set("${project.name}-${project.version}.jar")
    destinationDirectory.set(buildsDir)

    minimize()
  }
  clean {
    delete(buildsDir)
  }
}
