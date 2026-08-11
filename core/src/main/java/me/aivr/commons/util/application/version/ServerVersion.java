package me.aivr.commons.util.application.version;

/**
 * Contains parameters specific-to the minecraft-version the server's running as of now.
 *
 * @param major the major-digit for the version.
 * @param minor the minor-digit for the version.
 * @param patch the patch (or hotfix) digit for the version.
 * @param minecraftVersion the specific {@link MinecraftVersion} for the on-running version.
 * @since 0.1.0
 */
public record ServerVersion(byte major, byte minor, byte patch, MinecraftVersion minecraftVersion) {
  /**
   * A {@link ServerVersion} object that contains the information for the current server-version.
   *
   * @since 0.1.0
   */
  public static final ServerVersion CURRENT;
  /**
   * The stored result of the {@link #formatted()} method based on the {@link #CURRENT} version-reference.
   *
   * @since 0.1.0
   */
  public static final String CURRENT_FORMATED;

  static {
    final MinecraftVersion minecraftVersion = MinecraftVersion.fromServerVersion();
    if (minecraftVersion == null) {
      throw new ExceptionInInitializerError("Current minecraft-version is not supported by this library.");
    }
    final String[] versionNumbers = minecraftVersion.toVersionString().split("\\.", 3);
    final byte major = Byte.parseByte(versionNumbers[0]);
    final byte minor = Byte.parseByte(versionNumbers[1]);
    // Initial version-releases usually don't specify a patch (or hotfix) number. E.g. 26.1
    final byte patch = versionNumbers.length < 3 ? 0 : Byte.parseByte(versionNumbers[2]);

    CURRENT = new ServerVersion(major, minor, patch, minecraftVersion);
    CURRENT_FORMATED = CURRENT.formatted();
  }

  /**
   * Creates a new {@link ServerVersion} with the provided parameters.
   *
   * @param major the major-digit for the version.
   * @param minor the minor-digit for the version.
   * @param patch the patch (or hotfix) digit for the version.
   * @param minecraftVersion the {@link MinecraftVersion} constant for the on-running version.
   * @throws IllegalArgumentException if some, or all the version-digits are negative.
   * @since 0.1.0
   */
  public ServerVersion {
    if (major < 0 || minor < 0 || patch < 0) throw new IllegalArgumentException("Version-values cannot be negative.");
  }

  /**
   * Returns a new integer that contains both this version's major and minor digit, the values are represented as {@code {MAJOR}{MINOR}}
   * (e.g. {@code 26.1 -> 261}).
   *
   * @return an int that contains both major and minor-digits of the version.
   * @since 0.1.0
   */
  public int majorAndMinor() {
    return Integer.parseInt("" + this.major + this.minor);
  }

  /**
   * Processes this object's version-digits and returns a formatted-string to indicate the current version following the format
   * {@code v{MAJOR}_{MINOR}_[PATCH]}.
   * <p>
   * The patch-value is considered if it's greater than 1, otherwise, it will be ignored and the string will look like this
   * {@code v{MAJOR}_{MINOR}}.
   *
   * @return the formatted-version.
   * @since 0.1.0
   */
  public String formatted() {
    final StringBuilder builder = new StringBuilder();
    builder.append("v")
        .append(this.major)
        .append('_')
        .append(this.minor);
    if (this.patch > 0) {
      builder.append('_').append(this.patch);
    }
    return builder.toString();
  }
}
