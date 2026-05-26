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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import me.aivr.commons.config.application.Configuration;
import me.aivr.commons.config.application.Container;
import me.aivr.commons.config.infrastructure.ConfigType;
import me.aivr.commons.config.infrastructure.serializer.ComponentTypeSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

/**
 * Represents a container for internal data related to the YAML configuration-file.
 *
 * @param model the model used for configuration-access.
 * @param header the header to write in the config's file, if configured.
 * @param loader the loader for the model.
 * @param modelClass the class-type of the model.
 * @param <Config> a generic that indicates the model or class-type must extend from the {@link Configuration} contract.
 * @since 1.0.0
 */
@NullMarked
public record YamlConfigurationContainer<Config extends Configuration>(
    Config model,
    @Nullable String header,
    YamlConfigurationLoader loader,
    Class<Config> modelClass) implements Container<Config> {
  /**
   * Reloads the configuration contents asynchronously and returns a new container as result.
   *
   * @deprecated use {@link #reload()} instead.
   * @throws RuntimeException if an exception occurred during the process.
   * @return a {@link CompletableFuture} that contains the {@link YamlConfigurationContainer} returned by the operation.
   * @since 1.0.0
   */
  @Deprecated(since = "2.2.0")
  @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
  public CompletableFuture<YamlConfigurationContainer<Config>> reloadAsync() {
    return CompletableFuture.supplyAsync(this::reload0);
  }

  private YamlConfigurationContainer<Config> reload0() {
    try {
      final CommentedConfigurationNode node = this.loader.load();
      return new YamlConfigurationContainer<>(node.get(this.modelClass), this.header, this.loader, this.modelClass);
    } catch (final ConfigurateException exception) {
      throw new RuntimeException(exception);
    }
  }

  /**
   * Reloads the configuration contents asynchronously and returns a new container as result.
   *
   * @throws RuntimeException if an exception occurred during the process.
   * @return a {@link CompletableFuture} that contains the {@link YamlConfigurationContainer} returned by the operation.
   * @since 2.2.0
   */
  @Override
  public CompletableFuture<Container<Config>> reload() {
    return CompletableFuture.supplyAsync(this::reload0);
  }

  /**
   * Creates and returns a new {@link YamlConfigurationContainer} from the provided file parameters.
   * <p>
   * The {@code loader} for the container will be configured to use the {@code header} provided by the function, as well the
   * {@link ComponentTypeSerializer} implementation used for component-types support in configuration-models.
   *
   * @throws RuntimeException if an exception occurred during the process.
   * @param directory the directory of the file.
   * @param fileName the name of the file.
   * @param modelClass the model this file is represented by.
   * @param header the header to write in the file's header, {@code null} if none.
   * @param <C> a generic that indicates the class-type must extend from the {@link Configuration} contract.
   * @return the new {@link YamlConfigurationContainer}.
   * @since 1.0.0
   */
  public static <C extends Configuration> YamlConfigurationContainer<C> of(
      final Path directory,
      final String fileName,
      final Class<C> modelClass,
      final @Nullable String header) {
    final Path path = directory.resolve(fileName + ConfigType.YAML.fileExtension());
    final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
        .indent(2)
        .nodeStyle(NodeStyle.BLOCK)
        .defaultOptions(opts -> Container.BUILT_IN_OPTIONS.apply(opts, header))
        .path(path)
        .build();
    try {
      final CommentedConfigurationNode node = loader.load();
      final C config = node.get(modelClass);
      if (Files.notExists(path)) {
        node.set(modelClass, config);
        loader.save(node);
      }
      return new YamlConfigurationContainer<>(config, header, loader, modelClass);
    } catch (final ConfigurateException exception) {
      throw new RuntimeException(exception);
    }
  }
}
