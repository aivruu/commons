@file:Suppress("UnstableApiUsage")

pluginManagement {
  includeBuild("build-logic")
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "commons"
// single modules import.
listOf("core", "event", "parser").forEach { importProject(it) }

importProject("config")
listOf("yaml", "json").forEach { importProject(it, "config") }

listOf("json", "mongo", "local").forEach { importProject(it, "infrastructure") }

fun importProject(name: String) {
  importProject(name, null)
}

fun importProject(name: String, dir: String?) {
  val inSubDirectory = dir != null
  val validRoute = if (inSubDirectory) "$dir/$name" else name
  val kerbalProject = "${rootProject.name}${if (inSubDirectory) "-$dir" else ""}-$name"
  include(kerbalProject)
  project(":$kerbalProject").projectDir = file(validRoute)
}