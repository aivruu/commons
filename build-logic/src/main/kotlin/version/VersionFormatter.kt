package version

import org.gradle.api.Project

/**
 * Simple static-class that serves functions to format project-version with specific attributes.
 *
 * @since 3.0.0
 */
object VersionFormatter {
  /**
   * Includes additional-specifications to the project's version, including the release-candidate and current build-number
   * for the version-string, only if configured to do so.
   *
   * @param[target] the project to which modify its version-string.
   * @return the modified version.
   * @see provideReleaseCandidate
   * @see provideBuildNumber
   * @since 3.0.0
   */
  fun includeVersionSpecifications(target: Project): String {
    val shouldIncludeReleaseCandidate = (target.findProperty("include-release-candidate") as? String ?: "false").toBoolean()
    val shouldIncludeBuildNumber = (target.findProperty("include-build-number") as? String ?: "false").toBoolean()
    var versionDetails = ""
    if (shouldIncludeReleaseCandidate) {
      versionDetails = provideReleaseCandidate(target)
    }
    if (shouldIncludeBuildNumber) {
      versionDetails = "$versionDetails${provideBuildNumber(target)}"
    }
    // e.g. 3.4.3-rc3 or 3.4.3-rc2+build.43
    return "${target.version}$versionDetails"
  }

  /**
   * Returns a formatted-string that contains the current build-number for the given project.
   *
   * @param[target] the project from which read and write properties to.
   * @return the current build-number formatted.
   * @since 3.0.0
   */
  fun provideBuildNumber(target: Project): String {
    val buildNumber: Int = (target.findProperty("build-number") as? String ?: "1").toInt() // set as the first build
    return "+build.$buildNumber"
  }

  /**
   * Returns a formatted-string that contains the current release-candidate number for the given project.
   *
   * @param[target] the project from which read and write properties to.
   * @return the current release-candidate value formatted.
   * @since 3.0.0
   */
  fun provideReleaseCandidate(target: Project): String {
    val releaseCandidate: String = target.findProperty("release-candidate") as? String ?: "1" // set as the release-candidate
    return "-rc$releaseCandidate"
  }
}