// This file is part of "commons", licensed under the GNU License.
//
// Copyright (c) 2026 aivruu
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program. If not, see <https://www.gnu.org/licenses/>.
package me.aivr.commons.util.application.version;

import org.bukkit.Bukkit;
import org.jspecify.annotations.Nullable;

/**
 * Represents an enum that contains all the minecraft-versions from 1.21.1 up to latest-release.
 *
 * @since 3.2.0
 */
// https://github.com/HibiscusMC/HibiscusCommons/blob/master/common/src/main/java/me/lojosho/hibiscuscommons/nms/MinecraftVersion.java
public enum MinecraftVersion {
  v1_21_1("v1_21_R1"),
  v1_21_2("v1_21_R2"),
  v1_21_4("v1_21_R3"),
  v1_21_5("v1_21_R4"),
  v1_21_6("v1_21_R5"),
  v1_21_7("v1_21_R5"),
  v1_21_8("v1_21_R5"),
  v1_21_9("v1_21_R6"),
  v1_21_10("v1_21_R6"),
  v1_21_11("v1_21_R7"),
  v26_1("v26_1_R1"),
  v26_1_1("v26_1_R1"),
  v26_1_2("v26_1_R1"),
  v26_2("v26_2_R1");

  private final String mappingRevision;

  MinecraftVersion(final String mappingRevision) {
    this.mappingRevision = mappingRevision;
  }

  /**
   * Returns the value that corresponds to the mapping-revision for the current enum-constant's version.
   *
   * @return the version's mapping-revision (e.g. {@code v1_21_R3}).
   * @since 3.2.0
   */
  public String mappingRevision() {
    return this.mappingRevision;
  }

  /**
   * Checks whether the given {@link MinecraftVersion} constant is at a higher-position in the enum than the current one.
   *
   * @param other the enum-constant to check.
   * @return {@code true} if the specified version is higher than this.
   * @since 3.2.0
   */
  public boolean higher(final MinecraftVersion other) {
    return this.ordinal() > other.ordinal();
  }

  /**
   * Checks whether the given {@link MinecraftVersion} constant is at a higher-position or the same as the current one.
   *
   * @param other the enum-constant to check.
   * @return {@code true} if the specified version is higher than or equal to this.
   * @since 3.2.0
   */
  public boolean higherOrEqual(final MinecraftVersion other) {
    return this.ordinal() >= other.ordinal();
  }

  /**
   * Checks whether the given {@link MinecraftVersion} constant is at a lower-position or the same as the current one.
   *
   * @param other the enum-constant to check.
   * @return {@code true} if the specified version is lower than or equal to this.
   * @since 3.2.0
   */
  public boolean lowerOrEqual(final MinecraftVersion other) {
    return this.ordinal() <= other.ordinal();
  }

  /**
   * Checks whether the given {@link MinecraftVersion} constant is at a lower-position in the enum than the current one.
   *
   * @param other the enum-constant to check.
   * @return {@code true} if the specified version is lower than this.
   * @since 3.2.0
   */
  public boolean lower(final MinecraftVersion other) {
    return this.ordinal() < other.ordinal();
  }

  /**
   * Converts the enum into a usable string.
   *
   * @return Returns string of version (such as 1.21.4)
   * @since 3.2.0
   */
  public String toVersionString() {
    // Remove the "v" prefix and replace underscores with dots
    return this.name().substring(1).replace('_', '.');
  }

  /**
   * Returns the enum from a version. Returns null if invalid version.
   *
   * @param version A version number, such as 1.21.4
   * @return Returns the enum, such as v1_21_4
   * @since 3.2.0
   */
  public static @Nullable MinecraftVersion fromVersionString(final String version) {
    final String enumName = "v" + version.replace('.', '_');
    for (final MinecraftVersion v : values()) {
      if (v.name().equals(enumName)) {
        return v;
      }
    }
    return null;
  }

  /**
   * Returns the {@link MinecraftVersion} constant, if available, that correspond depending on the running-version for the server.
   *
   * @return the {@link MinecraftVersion} for the current server-version, {@code null} if the version isn't specified in the enum.
   * @see #fromVersionString(String)
   * @since 3.2.0
   */
  public static @Nullable MinecraftVersion fromServerVersion() {
    // https://github.com/HibiscusMC/HibiscusCommons/blob/master/common/src/main/java/me/lojosho/hibiscuscommons/nms/NMSHandlers.java#L108
    final String bukkitVersion = Bukkit.getBukkitVersion();
    String minecraftVersion = bukkitVersion.substring(0, bukkitVersion.indexOf('-')); // Legacy-wise this is enough
    if (minecraftVersion.contains("build")) {
      // Paper new 26.1+ versioning system; Ex. 26.1.2.build.51-beta
      minecraftVersion = minecraftVersion.substring(0, minecraftVersion.indexOf(".build"));
    }
    return fromVersionString(minecraftVersion);
  }
}
