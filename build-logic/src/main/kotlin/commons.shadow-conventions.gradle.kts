import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar.Companion.shadowJar

plugins {
  id("commons.publish-conventions")
  id("com.gradleup.shadow")
}

val buildsDir: File = file("$rootDir/jars")

tasks {
  shadowJar {
    archiveClassifier.set("")
    archiveFileName.set(project.name + ".jar")

    destinationDirectory.set(buildsDir)

    minimize()
  }
  clean {
    delete(buildsDir)
  }
}
