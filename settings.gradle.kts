@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "commons"

sequenceOf("api", "core").forEach {
    val kerbalProject = "${rootProject.name}-$it"
    include(kerbalProject)
    project(":$kerbalProject").projectDir = file(it)
}

sequenceOf("json", "mongo", "local").forEach {
    val kerbalProject = "${rootProject.name}-infrastructure-$it"
    include(kerbalProject)
    project(":$kerbalProject").projectDir = file("infrastructure/$it")
}