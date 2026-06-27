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

import me.aivr.commons.config.application.Configuration;
import me.aivr.commons.config.application.ConfigurationProvider;
import me.aivr.commons.config.application.Container;
import me.aivr.commons.config.infrastructure.container.ContainerBuilder;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implementation that provides common-logic across providers, as well, implement common functions' logic.
 *
 * @param <Config> a generic that indicates the model this provider handles must extend from the {@link Configuration} contract.
 * @since 1.0.0
 */
public class ConfigurationProviderImpl<Config extends Configuration> implements ConfigurationProvider<Config> {
  protected final Path directory;
  protected final String fileName;
  protected final Class<Config> type;
  protected final ComponentLogger logger;
  protected @Nullable Container<Config> configContainer;

  /**
   * Creates a new {@link ConfigurationProviderImpl} with the provided parameters.
   * <p>
   * This constructor will create a new {@link ComponentLogger} object using this class's name for the logger.
   *
   * @param directory the file's directory.
   * @param fileName the file's name.
   * @param type the model's type.
   * @since 3.0.0
   */
  public ConfigurationProviderImpl(final Path directory, final String fileName, final Class<Config> type) {
    this(directory, fileName, type, ComponentLogger.logger(ConfigurationProviderImpl.class));
  }

  /**
   * Creates a new {@link ConfigurationProviderImpl} with the provided parameters.
   *
   * @param directory the file's directory.
   * @param fileName the file's name.
   * @param type the model's type.
   * @param logger the logger used for provider-related operations.
   * @since 3.0.0
   */
  public ConfigurationProviderImpl(final Path directory, final String fileName, final Class<Config> type, final ComponentLogger logger) {
    this.directory = directory;
    this.fileName = fileName;
    this.type = type;
    this.logger = logger;
  }

  @Override
  public Path configDirectory() {
    return this.directory;
  }

  @Override
  public String configName() {
    return this.fileName;
  }

  @Override
  public Class<Config> modelClassType() {
    return this.type;
  }

  @Override
  public @Nullable Container<Config> internalContainer() {
    return this.configContainer;
  }

  /**
   * {@inheritDoc}
   * <p>
   * If there's an exception during configuration-loading, this function will handle it and notify it using this class's {@link #logger}.
   *
   * @since 3.0.0
   */
  @Override
  public boolean load(final ContainerBuilder builder) {
    try {
      this.configContainer = builder.build();
      return true;
    } catch (final RuntimeException exception) {
      this.logger.error("Failed to load {}'s configuration-data from model {}.", this.fileName, this.type.getSimpleName(), exception);
      return false;
    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * If this function is requested to reload the configuration-model async, it will provide additional handling for the operation
   * and will handle any exception/error that may happen during the process.
   *
   * @since 3.0.0
   */
  @Override
  public boolean reload(final boolean async) {
    if (this.configContainer == null) return false;

    if (!async) {
      return this.configContainer.reload();
    }
    // handle async-computation
    final AtomicBoolean atomicResult = new AtomicBoolean();
    this.configContainer.reloadAsync()
        .exceptionally(exception -> {
          // As the function re-throws the caught exception, we need to handle it here.
          this.logger.error("Unexpected error when handling async-reload for file '{}'.", this.fileName, exception);
          return false;
        });
    return atomicResult.get();
  }

  @Override
  public final Config get() {
    if (this.configContainer == null) throw ConfigurationProvider.NOT_LOADED_CONFIGURATION_EXCEPTION;
    return this.configContainer.model();
  }
}
