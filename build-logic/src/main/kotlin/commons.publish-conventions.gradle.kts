plugins {
  id("commons.common-conventions")
  `maven-publish`
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      artifactId = artifactId.substring(rootProject.name.length + 1)

      from(components["java"])
    }
  }
}