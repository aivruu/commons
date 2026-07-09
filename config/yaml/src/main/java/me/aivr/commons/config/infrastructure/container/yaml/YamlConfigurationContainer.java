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
package me.aivr.commons.config.infrastructure.container.yaml;

import me.aivr.commons.config.application.Configuration;
import me.aivr.commons.config.application.Container;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

/**
 * A mutable-container for single YAML-related configuration-data storage.
 *
 * @param <Config> an object that extends from the {@link Configuration} interface.
 * @since 3.0.0-rc1
 */
public final class YamlConfigurationContainer<Config extends Configuration> implements Container<Config> {
  private final @Nullable String header;
  private final Class<? extends Configuration> clazzType;
  private final YamlConfigurationLoader loader;
  private Config model;

  /**
   * Creates a new {@link YamlConfigurationContainer} with the provided parameters.
   *
   * @param header the header for the config's file, {@code null} if none.
   * @param clazzType the class-type of the config-model.
   * @param loader the configuration loader.
   * @param model the actual configuration-model already loaded.
   * @since 3.0.0-rc1
   */
  public YamlConfigurationContainer(
      final @Nullable String header,
      final Class<? extends Configuration> clazzType,
      final YamlConfigurationLoader loader,
      final Config model) {
    this.header = header;
    this.clazzType = clazzType;
    this.loader = loader;
    this.model = model;
  }

  @Override
  public Config model() {
    return this.model;
  }

  @Override
  public @Nullable String header() {
    return this.header;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Class<Config> modelClass() {
    return (Class<Config>) this.clazzType;
  }

  @Override
  @SuppressWarnings({"unchecked", "ConstantConditions"}) // ignore unchecked-cast, newConfig won't be null
  public boolean reload() {
    try {
      final CommentedConfigurationNode node = this.loader.load();
      final Config newConfig = (Config) node.get(this.clazzType);
      if (newConfig == this.model) {
        return false;
      }
      this.model = newConfig;
      return true;
    } catch (final ConfigurateException exception) {
      throw new RuntimeException(exception);
    }
  }
}
