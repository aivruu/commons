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
package me.aivr.commons.config.infrastructure.container.json;

import com.google.common.base.Preconditions;
import me.aivr.commons.config.application.Configuration;
import me.aivr.commons.config.application.Container;
import me.aivr.commons.config.infrastructure.ConfigType;
import me.aivr.commons.config.infrastructure.container.ContainerBuilder;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.UnaryOperator;

/**
 * Represents a {@link ContainerBuilder} type for {@link JsonConfigurationContainer}s.
 *
 * @since 3.0.0
 */
public final class JsonContainerBuilder extends ContainerBuilder {
  /**
   * Creates a new {@link JsonContainerBuilder} from the provided parameter.
   *
   * @param directory the directory to use to resolve the JSON-file.
   * @since 3.0.0
   */
  public JsonContainerBuilder(final Path directory) {
    super(directory);
  }

  /**
   * Creates a new {@link JsonContainerBuilder} from the provided {@code directory} parameter.
   *
   * @param directory the directory where store the configuration-file.
   * @return a new {@link JsonContainerBuilder}.
   * @since 3.0.0
   */
  public static JsonContainerBuilder create(final Path directory) {
    return new JsonContainerBuilder(directory);
  }

  @Override
  protected UnaryOperator<ConfigurationOptions> resolveOptions() {
    return super.configOptions != null ? super.configOptions : opts -> Container.BUILT_IN_OPTIONS.apply(opts, null);
  }

  @Override
  @SuppressWarnings({"unchecked", "ConstantConditions"}) // ignore unchecked-cast, suppress node#get null-warning
  public <C extends Configuration> Container<C> build() {
    Preconditions.checkNotNull(super.configClass, "Unspecified config class-type for JSON-container construction.");
    Preconditions.checkArgument(super.fileName != null && !super.fileName.isEmpty(),
        "Unspecified or empty file-name for JSON-configuration construction.");

    final Path filePath = super.directory.resolve(super.fileName + ConfigType.JSON.fileExtension());
    final GsonConfigurationLoader loader = GsonConfigurationLoader.builder()
        .lenient(true)
        .defaultOptions(this.resolveOptions())
        .path(filePath)
        .build();
    try {
      final BasicConfigurationNode node = loader.load();
      final C config = (C) node.get(super.configClass);
      if (Files.notExists(filePath)) {
        node.set(super.configClass, config);
        loader.save(node);
      }
      return new JsonConfigurationContainer<>(super.configClass, loader, config);
    } catch (final ConfigurateException exception) {
      throw new RuntimeException(exception);
    }
  }
}
