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

import me.aivr.commons.config.application.Configuration;
import me.aivr.commons.config.application.Container;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationOptions;

import java.nio.file.Path;
import java.util.function.UnaryOperator;

/**
 * Represents a builder for {@link Container}s construction.
 *
 * @since 3.0.0
 */
public abstract class ContainerBuilder {
  protected final Path directory;
  protected @Nullable String fileName;
  protected @Nullable Class<? extends Configuration> configClass;
  protected @Nullable UnaryOperator<ConfigurationOptions> configOptions;

  /**
   * Creates a new {@link ContainerBuilder} from the provided parameter.
   *
   * @param directory the directory to use for the file.
   * @since 3.0.0
   */
  protected ContainerBuilder(final Path directory) {
    this.directory = directory;
  }

  /**
   * Sets the name of the configuration-file.
   *
   * @param name the file's name.
   * @return this builder.
   * @since 3.0.0
   */
  public ContainerBuilder fileName(final String name) {
    this.fileName = name;
    return this;
  }

  /**
   * Sets the class-model to use for the configuration.
   *
   * @param configClass the model to use.
   * @return this builder.
   * @since 3.0.0
   */
  public ContainerBuilder clazz(final Class<? extends Configuration> configClass) {
    this.configClass = configClass;
    return this;
  }

  /**
   * Specifies the options to use for the configuration generation.
   *
   * @param opts the options for the configuration-file.
   * @return this builder.
   * @since 3.0.0
   */
  public ContainerBuilder options(final UnaryOperator<ConfigurationOptions> opts) {
    this.configOptions = opts;
    return this;
  }

  /**
   * Resolves the custom config-options provided by {@link #options(UnaryOperator)} if available, otherwise, it will use the built-in
   * options instead.
   *
   * @return the resolved options.
   * @see Container#BUILT_IN_OPTIONS Default configuration options
   * @since 3.0.0
   */
  protected abstract UnaryOperator<ConfigurationOptions> resolveOptions();

  /**
   * Creates a new {@link Container} using the information of this builder.
   *
   * @return a new {@link Container}.
   * @since 3.0.0
   */
  public abstract <C extends Configuration> Container<C> build();
}
