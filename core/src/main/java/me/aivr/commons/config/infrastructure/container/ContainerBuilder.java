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
package me.aivr.commons.config.infrastructure.container;

import java.nio.file.Path;
import java.util.function.UnaryOperator;
import me.aivr.commons.config.application.Configuration;
import me.aivr.commons.config.application.Container;
import me.aivr.commons.config.infrastructure.ConfigType;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationOptions;

/**
 * Represents a builder for {@link Container}s construction.
 *
 * @param <Config> a class that extends from {@link Configuration}.
 * @since 2.4.0
 */
public abstract class ContainerBuilder<Config extends Configuration> {
  protected final Path directory;
  protected @Nullable ConfigType type;
  protected @Nullable String fileName;
  protected @Nullable Class<Config> configClass;
  protected @Nullable UnaryOperator<ConfigurationOptions> configOptions;

  /**
   * Creates a new {@link ContainerBuilder} from the provided parameter.
   *
   * @param directory the directory to use for the file.
   * @since 2.4.0
   */
  protected ContainerBuilder(final Path directory) {
    this.directory = directory;
  }

  /**
   * Specifies the file-type to use for the configuration.
   *
   * @param configType the {@link ConfigType} to set.
   * @return this builder.
   * @since 2.4.0
   */
  public ContainerBuilder<Config> type(final ConfigType configType) {
    this.type = configType;
    return this;
  }

  /**
   * Sets the name of the configuration-file.
   *
   * @param name the file's name.
   * @return this builder.
   * @since 2.4.0
   */
  public ContainerBuilder<Config> fileName(final String name) {
    this.fileName = name;
    return this;
  }

  /**
   * Sets the class-model to use for the configuration.
   *
   * @param configClass the model to use.
   * @return this builder.
   * @since 2.4.0
   */
  public ContainerBuilder<Config> clazz(final Class<Config> configClass) {
    this.configClass = configClass;
    return this;
  }

  /**
   * Specifies the options to use for the configuration generation.
   *
   * @param opts the options for the configuration-file.
   * @return this builder.
   * @since 2.4.0
   */
  public ContainerBuilder<Config> options(final UnaryOperator<ConfigurationOptions> opts) {
    this.configOptions = opts;
    return this;
  }

  /**
   * Creates a new {@link Container} using the information of this builder.
   *
   * @return a new {@link Container}.
   * @since 2.4.0
   */
  public abstract Container<Config> build();
}
