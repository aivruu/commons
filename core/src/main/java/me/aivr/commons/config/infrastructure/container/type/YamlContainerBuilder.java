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
package me.aivr.commons.config.infrastructure.container.type;

import com.google.common.base.Preconditions;
import java.nio.file.Files;
import java.nio.file.Path;
import me.aivr.commons.config.application.Configuration;
import me.aivr.commons.config.application.Container;
import me.aivr.commons.config.infrastructure.ConfigType;
import me.aivr.commons.config.infrastructure.container.ContainerBuilder;
import me.aivr.commons.config.infrastructure.container.YamlConfigurationContainer;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

/**
 * Represents a {@link ContainerBuilder} type for {@link YamlConfigurationContainer}s.
 *
 * @param <Config> a class that extends from {@link Configuration}.
 * @since 2.4.0
 */
public final class YamlContainerBuilder<Config extends Configuration> extends ContainerBuilder<Config> {
  private @Nullable String header;

  /**
   * Creates a new {@link YamlContainerBuilder} from the provided parameter.
   *
   * @param directory the directory to use to resolve the YAML-file.
   * @since 2.4.0
   */
  public YamlContainerBuilder(final Path directory) {
    super(directory);
  }

  /**
   * Creates a new {@link YamlContainerBuilder} from the provided {@code directory} parameter.
   *
   * @param directory the directory where store the configuration-file.
   * @param <Config> a class that extends from {@link Configuration}.
   * @return a new {@link YamlContainerBuilder}.
   * @since 2.4.0
   */
  public static <Config extends Configuration> YamlContainerBuilder<Config> create(final Path directory) {
    return new YamlContainerBuilder<>(directory);
  }

  /**
   * Sets the header to write to the file.
   *
   * @param header the header for the config-file.
   * @return this builder.
   * @since 2.4.0
   */
  public YamlContainerBuilder<Config> header(final @Nullable String header) {
    this.header = header;
    return this;
  }

  @Override
  public Container<Config> build() {
    Preconditions.checkNotNull(super.configClass, "Unspecified config class-type for YAML-container construction.");
    Preconditions.checkNotNull(super.fileName, "Unspecified file-name for YAML-configuration construction.");
    Preconditions.checkArgument(!super.fileName.isEmpty(), "Empty file-name for YAML-configuration construction.");

    final Path path = super.directory.resolve(super.fileName + ConfigType.YAML.fileExtension());
    final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
        .indent(2)
        .nodeStyle(NodeStyle.BLOCK)
        .defaultOptions(super.configOptions == null ? opts -> Container.BUILT_IN_OPTIONS.apply(opts, this.header) : super.configOptions)
        .path(path)
        .build();
    try {
      final CommentedConfigurationNode node = loader.load();
      final Config config = node.get(super.configClass);
      if (Files.notExists(path)) {
        node.set(super.configClass, config);
        loader.save(node);
      }
      return new YamlConfigurationContainer<>(config, this.header, loader, super.configClass);
    } catch (final ConfigurateException exception) {
      throw new RuntimeException(exception);
    }
  }
}
