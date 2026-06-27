@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "commons"

listOf("api", "core").forEach { importProject(it, null) }

// config-related subprojects
importProject("config", null)
listOf("yaml", "json", "toml").forEach { importProject(it, "config") }

// infrastructure-related
listOf("json", "mongo", "local").forEach { importProject(it, "infrastructure") }

fun importProject(name: String, dir: String?) {
  val inSubDirectory = dir != null
  val validRoute = if (inSubDirectory) "$dir/$name" else name
  val kerbalProject = "${rootProject.name}${if (inSubDirectory) "-$dir" else ""}-$name"
  include(kerbalProject)
  project(":$kerbalProject").projectDir = file(validRoute)
}