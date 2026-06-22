import groovy.util.Node

plugins {
  id("commons.common-conventions")
  `maven-publish`
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      artifactId = artifactId.substring(rootProject.name.length + 1)
      from(components["java"])

      // exclude custom-fork artifacts
      if (artifactId.equals("core")) {
        pom {
          withXml {
            asNode().children().filterIsInstance<Node>()
              .find { it.name() == "dependencies" }
              ?.children()?.removeIf { dep ->
                (dep as? Node)?.let {
                  (it.get("artifactId") as? Node)?.text() == "configurate"
                } ?: false
              }
          }
        }
      }
    }
  }
}