import groovy.util.Node
import groovy.xml.XmlUtil

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
      pom {
        withXml {
          // all this because of a local-lib lol
          val root = asNode()
          val deps = root.children().filterIsInstance<Node>()
            .find { it.name().toString().endsWith("dependencies") } ?: return@withXml

          val snapshot = deps.children().toList()
          deps.children().clear()

          for (child in snapshot) {
            val node = child as? Node ?: run { deps.children().add(child); continue }

            val aid = node.children().filterIsInstance<Node>()
              .find { it.name().toString().endsWith("artifactId") }
              ?.children()?.firstOrNull()?.toString()?.trim()

            val gid = node.children().filterIsInstance<Node>()
              .find { it.name().toString().endsWith("groupId") }
              ?.children()?.firstOrNull()?.toString()?.trim()

            val isConfigurate = aid?.contains("configurate", ignoreCase = true) == true
            val isSnapshot = aid?.endsWith("-SNAPSHOT") == true
            val isLocal = gid.isNullOrBlank() || isSnapshot

            if (isConfigurate && isLocal) {
              logger.lifecycle("Removing dependency artifactId=$aid groupId=$gid")
            } else {
              deps.children().add(node)
            }
          }
        }
      }
    }
  }
}