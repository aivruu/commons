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
   * Loads the configuration-data for this provider's model.
   *
   * @return {@code true} if the model was loaded correctly, {@code false} otherwise.
   * @since 1.0.0
   */
  boolean load();

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
