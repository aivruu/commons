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

import me.aivr.commons.config.infrastructure.container.ContainerBuilder;
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
   * Returns the class-type for the configuration-model handled by this provider.
   *
   * @return the configuration-model's class-type.
   * @since 3.0.0
   */
  Class<Config> modelClassType();

  /**
   * Returns the {@link Container} for this provider's.
   *
   * @return the configuration-container for the file, or {@code null} if the configuration hasn't been loaded yet.
   * @since 2.3.0
   */
  @Nullable Container<Config> internalContainer();

  /**
   * Loads the configuration-data for this provider's model.
   *
   * @param builder the builder used for attributes-specification.
   * @return {@code true} if the model was loaded correctly, {@code false} otherwise.
   * @since 2.3.0
   */
  boolean load(final ContainerBuilder builder);

  /**
   * Reloads the configuration-data for this provider's model.
   *
   * @param async whether the reload-process should be executed asynchronously.
   * @return {@code true} if the model was reloaded correctly, {@code false} otherwise.
   * @since 3.0.0
   */
  boolean reload(final boolean async);

  /**
   * Returns the configuration-model for this provider, if available.
   *
   * @throws IllegalStateException if the configuration hasn't been loaded yet.
   * @return the configuration-model.
   * @since 1.0.0
   */
  Config get();
}
