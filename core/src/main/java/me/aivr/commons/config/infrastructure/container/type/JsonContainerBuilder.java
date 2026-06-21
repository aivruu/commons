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
import me.aivr.commons.config.application.Configuration;
import me.aivr.commons.config.application.Container;
import me.aivr.commons.config.infrastructure.ConfigType;
import me.aivr.commons.config.infrastructure.container.ContainerBuilder;
import me.aivr.commons.config.infrastructure.container.JsonConfigurationContainer;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Represents a {@link ContainerBuilder} type for {@link JsonConfigurationContainer}s.
 *
 * @param <Config> a class that extends from {@link Configuration}.
 * @since 2.4.0
 */
public final class JsonContainerBuilder<Config extends Configuration> extends ContainerBuilder<Config> {
  /**
   * Creates a new {@link JsonContainerBuilder} from the provided parameter.
   *
   * @param directory the directory to use to resolve the JSON-file.
   * @since 2.4.0
   */
  public JsonContainerBuilder(final Path directory) {
    super(directory);
  }

  /**
   * Creates a new {@link JsonContainerBuilder} from the provided {@code directory} parameter.
   *
   * @param directory the directory where store the configuration-file.
   * @param <Config> a class that extends from {@link Configuration}.
   * @return a new {@link JsonContainerBuilder}.
   * @since 2.4.0
   */
  public static <Config extends Configuration> JsonContainerBuilder<Config> create(final Path directory) {
    return new JsonContainerBuilder<>(directory);
  }

  @Override
  public Container<Config> build() {
    Preconditions.checkNotNull(super.configClass, "Unspecified config class-type for JSON-container construction.");
    Preconditions.checkNotNull(super.fileName, "Unspecified file-name for JSON-configuration construction.");
    Preconditions.checkArgument(!super.fileName.isEmpty(), "Empty file-name for JSON-configuration construction.");

    final Path filePath = super.directory.resolve(super.fileName + ConfigType.JSON.fileExtension());
    final GsonConfigurationLoader loader = GsonConfigurationLoader.builder()
        .lenient(true)
        .defaultOptions(super.configOptions == null ? opts -> Container.BUILT_IN_OPTIONS.apply(opts, null) : super.configOptions)
        .path(filePath)
        .build();
    try {
      final BasicConfigurationNode node = loader.load();
      final Config config = node.get(super.configClass);
      if (Files.notExists(filePath)) {
        node.set(super.configClass, config);
        loader.save(node);
      }
      return new JsonConfigurationContainer<>(config, loader, super.configClass);
    } catch (final ConfigurateException exception) {
      throw new RuntimeException(exception);
    }
  }
}
