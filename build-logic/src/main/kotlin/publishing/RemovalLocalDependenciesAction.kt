package publishing

import org.gradle.api.Action
import org.gradle.api.XmlProvider
import groovy.util.Node

class RemovaLocalDependenciesAction: Action<XmlProvider> {
  override fun execute(provider: XmlProvider) {
    val root = provider.asNode()
    val deps = root.children()
      .filterIsInstance<Node>()
      .find { it.name().toString().endsWith("dependencies") } ?: return

    val snapshot = deps.children().toList()
    deps.children().clear()
    for (child in snapshot) {
      val node = child as? Node ?: run { deps.children().add(child); continue }
      val artifact = node.children()
        .filterIsInstance<Node>()
        .find { it.name().toString().endsWith("artifactId") }
        ?.children()
        ?.firstOrNull()
        ?.toString()
        ?.trim()
      val group = node.children()
        .filterIsInstance<Node>()
        .find { it.name().toString().endsWith("groupId") }
        ?.children()
        ?.firstOrNull()
        ?.toString()
        ?.trim()
      val isConfigurate = artifact?.contains("configurate", ignoreCase = true) == true
      val isSnapshot = artifact?.endsWith("-SNAPSHOT") == true
      val isLocal = group.isNullOrBlank() || isSnapshot
      if (isConfigurate && isLocal) {
        println("Removing local configurate-dependency: groupId=$group artifactId=$artifact")
      } else {
        deps.children().add(node)
      }
    }
  }
}