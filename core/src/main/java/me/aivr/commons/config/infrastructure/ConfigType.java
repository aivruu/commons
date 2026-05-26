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
package me.aivr.commons.config.infrastructure;

import org.jspecify.annotations.NullMarked;

/**
 * Contains the configuration-types that can be used, config-types may or not as well be handled by the same provider
 * (e.g. Configurate).
 *
 * @since 2.3.0
 */
@NullMarked
public enum ConfigType {
  YAML(".yml"),
  JSON(".json"),
  TOML(".toml"); // not implemented yet.

  private final String fileExtension;

  ConfigType(final String fileExtension) {
    this.fileExtension = fileExtension;
  }

  /**
   * Returns this enum-constant's value (extension for the file).
   *
   * @return the file extension.
   * @since 2.3.0
   */
  public String fileExtension() {
    return this.fileExtension;
  }
}
