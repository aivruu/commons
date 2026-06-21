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
package me.aivr.commons.config.application;

import org.jspecify.annotations.Nullable;

import java.nio.file.Path;

/**
 * Represents a provider that allows to handle and access a configuration-model dynamically.
 *
 * @param <Config> a generic that indicates the model must extend from {@link Configuration} contract.
 * @since 1.0.0
 */
public interface ConfigurationProvider<Config extends Configuration> {
  /**
   * Cached-exception thrown each time this provider's model is intended to be accessed without have been initialized
   * yet.
   *
   * @since 1.0.0
   */
  IllegalStateException NOT_LOADED_CONFIGURATION_EXCEPTION = new IllegalStateException("The configuration has not been loaded yet.");

  /**
   * Returns the {@link Path} directory for this provider's configuration.
   *
   * @return the directory-path of the configuration-file.
   * @since 2.3.0
   */
  Path configDirectory();

  /**
   * Returns the name of the configuration-file.
   *
   * @return the configuration-file's full name.
   * @since 2.3.0
   */
  String configName();

  /**
   * Returns the {@link Container} for this provider's.
   *
   * @return the configuration-container for the file, or {@code null} if the configuration hasn't been loaded yet.
   * @since 2.3.0
   */
  @Nullable Container<?> internalContainer();

  /**
   * Loads the configuration-data for this provider's model.
   *
   * @deprecated still uses and only supports YAML format, use {@link #load(String)} instead.
   * @return {@code true} if the model was loaded correctly, {@code false} otherwise.
   * @since 1.0.0
   */
  @Deprecated(since = "2.3.0")
  boolean load();

  /**
   * Loads the configuration-data for this provider's model, this function will apply no header to the file.
   *
   * @return {@code true} if the model was loaded correctly, {@code false} otherwise.
   * @since 2.5.0
   */
  default boolean loadWithoutHeader() {
    return this.load(null);
  }

  /**
   * Loads the configuration-data for this provider's model.
   *
   * @param header the header for the configuration-file, {@code null} if none.
   * @return {@code true} if the model was loaded correctly, {@code false} otherwise.
   * @since 2.3.0
   */
  boolean load(final @Nullable String header);

  /**
   * Reloads the configuration-data for this provider's model.
   *
   * @return {@code true} if the model was reloaded correctly, {@code false} otherwise.
   * @since 1.0.0
   */
  boolean reload();

  /**
   * Returns the configuration-model for this provider, if available.
   *
   * @throws IllegalStateException if the configuration hasn't been loaded yet.
   * @return the configuration-model.
   * @since 1.0.0
   */
  Config get();
}
