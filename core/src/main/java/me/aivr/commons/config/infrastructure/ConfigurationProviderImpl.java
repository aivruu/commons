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

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;
import me.aivr.commons.config.application.Configuration;
import me.aivr.commons.config.application.ConfigurationProvider;
import me.aivr.commons.config.application.Container;
import me.aivr.commons.config.infrastructure.container.JsonConfigurationContainer;
import me.aivr.commons.config.infrastructure.container.YamlConfigurationContainer;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Implementation that provides common-logic across providers, as well, implement common functions' logic.
 *
 * @param <Config> a generic that indicates the model this provider handles must extend from the {@link Configuration} contract.
 * @since 1.0.0
 */
@NullMarked
public class ConfigurationProviderImpl<Config extends Configuration> implements ConfigurationProvider<Config> {
  protected final Path directory;
  protected final String fileName;
  protected final ConfigType fileType;
  protected final Class<Config> type;
  protected final ComponentLogger logger;
  protected @Nullable Container<Config> configContainer;

  @Deprecated(since = "2.3.0", forRemoval = true)
  private @Nullable ConfigurationProvider<Config> container;

  /**
   * Creates a new {@link ConfigurationProviderImpl} with the provided parameters.
   *
   * @param directory the file's directory.
   * @param fileName the file's name.
   * @param type the model's type.
   * @param logger the logger used for provider-related operations.
   * @since 1.0.0
   */
  public ConfigurationProviderImpl(
      final Path directory,
      final String fileName,
      final ConfigType fileType,
      final Class<Config> type,
      final ComponentLogger logger) {
    this.directory = directory;
    this.fileName = fileName;
    this.fileType = fileType;
    this.type = type;
    this.logger = logger;
  }

  @Override
  public Path configDirectory() {
    return this.directory;
  }

  @Override
  public String configName() {
    return this.fileName + this.fileType.fileExtension();
  }

  @Override
  public @Nullable Container<?> internalContainer() {
    return this.configContainer;
  }

  @Override
  @Deprecated
  public boolean load() {
    try {
//      this.configContainer = ConfigurationContainer.of(this.directory, this.fileName, this.type);
      return true;
    } catch (final RuntimeException exception) {
      this.logger.error("Failed to load {}'s configuration-data from model {}.", this.fileName, this.type.getSimpleName(), exception);
      return false;
    }
  }

  @Override
  public boolean load(final @Nullable String header) {
    try {
      this.configContainer = switch (this.fileType) {
        // TOML-support isn't implemented yet so assume it's YAML instead
        case YAML, TOML -> YamlConfigurationContainer.of(this.directory, this.fileName, this.type, header);
        case JSON -> JsonConfigurationContainer.of(this.directory, this.fileName, this.type);
      };
      return true;
    } catch (final RuntimeException exception) {
      this.logger.error("Failed to load {}'s configuration-data from model {}.", this.fileName, this.type.getSimpleName(), exception);
      return false;
    }
  }

  @Override
  public boolean reload() {
    if (this.configContainer == null) return false;

    final AtomicReference<@Nullable Container<Config>> newContainer = new AtomicReference<>();
    this.configContainer.reload()
        .exceptionally(exception -> {
          // As the function re-throws the caught exception, we need to handle it here.
          this.logger.error("Unexpected error when handling async-reload for file '{}'.", this.fileName, exception);
          return null;
        })
        .thenAccept(container -> {
          // Though function itself doesn't return null, the exception-handling [exceptionally()] for this async-computation yes it does.
          if (container != null) {
            newContainer.set(container);
          }
        });
    final Container<Config> updatedContainer = newContainer.get();
    if (updatedContainer == null) {
      return false;
    }
    this.configContainer = updatedContainer;
    return true;
  }

  @Override
  public final Config get() {
    if (this.configContainer == null) throw ConfigurationProvider.NOT_LOADED_CONFIGURATION_EXCEPTION;
    return this.configContainer.model();
  }
}
