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

import com.google.common.base.Preconditions;
import me.aivr.commons.config.application.Configuration;
import me.aivr.commons.config.application.Container;
import me.aivr.commons.config.infrastructure.ConfigType;
import me.aivr.commons.config.infrastructure.container.ContainerBuilder;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.UnaryOperator;

/**
 * Represents a {@link ContainerBuilder} type for {@link YamlConfigurationContainer}s.
 *
 * @since 3.0.0
 */
public final class YamlContainerBuilder extends ContainerBuilder {
  private @Nullable String header;

  /**
   * Creates a new {@link YamlContainerBuilder} from the provided parameter.
   *
   * @param directory the directory to use to resolve the YAML-file.
   * @since 3.0.0
   */
  public YamlContainerBuilder(final Path directory) {
    super(directory);
  }

  /**
   * Creates a new {@link YamlContainerBuilder} from the provided {@code directory} parameter.
   *
   * @param directory the directory where store the configuration-file.
   * @return a new {@link YamlContainerBuilder}.
   * @since 3.0.0
   */
  public static YamlContainerBuilder create(final Path directory) {
    return new YamlContainerBuilder(directory);
  }

  /**
   * Sets the header to write to the file.
   *
   * @param header the header for the config-file.
   * @return this builder.
   * @since 3.0.0
   */
  public YamlContainerBuilder header(final @Nullable String header) {
    this.header = header;
    return this;
  }

  @Override
  protected UnaryOperator<ConfigurationOptions> resolveOptions() {
    return opts -> {
      if (super.configOptions == null) {
        return Container.BUILT_IN_OPTIONS.apply(opts, this.header);
      }
      ConfigurationOptions resolvedProvidedOptions = super.configOptions.apply(opts);
      // Apply header if it wasn't configured in the custom-options for the file.
      if (this.header != null && resolvedProvidedOptions.header() == null) {
        resolvedProvidedOptions = resolvedProvidedOptions.header(this.header);
      }
      return resolvedProvidedOptions;
    };
  }

  @Override
  @SuppressWarnings({"unchecked", "ConstantConditions"}) // ignore unchecked-cast, suppress node#get null-warning
  public <C extends Configuration> Container<C> build() {
    Preconditions.checkNotNull(super.configClass, "Unspecified config class-type for YAML-container construction.");
    Preconditions.checkArgument(super.fileName != null && !super.fileName.isEmpty(),
        "Unspecified or empty file-name for YAML-configuration construction.");

    final Path path = super.directory.resolve(super.fileName + ConfigType.YAML.fileExtension());
    final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
        .nodeStyle(NodeStyle.BLOCK)
        .commentsEnabled(true) // finally
        .defaultOptions(this.resolveOptions())
        .path(path)
        .build();
    try {
      final CommentedConfigurationNode node = loader.load();
      final C config = (C) node.get(super.configClass);
      if (Files.notExists(path)) {
        node.set(super.configClass, config);
        loader.save(node);
      }
      return new YamlConfigurationContainer<>(this.header, super.configClass, loader, config);
    } catch (final ConfigurateException exception) {
      throw new RuntimeException(exception);
    }
  }
}
