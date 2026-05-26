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
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

/**
 * Represents a container for internal data related to the JSON configuration-file.
 *
 * @param model the model used for configuration-access.
 * @param loader the loader for the model.
 * @param modelClass the class-type of the model.
 * @param <Config> a generic that indicates the model or class-type must extend from the {@link Configuration} contract.
 * @since 2.3.0
 */
@NullMarked
public record JsonConfigurationContainer<Config extends Configuration>(
    Config model,
    GsonConfigurationLoader loader,
    Class<Config> modelClass) implements Container<Config> {
  @Override
  public @Nullable String header() {
    return null;
  }

  @Override
  public CompletableFuture<Container<Config>> reload() {
    return CompletableFuture.supplyAsync(() -> {
      try {
        final BasicConfigurationNode node = this.loader.load();
        return new JsonConfigurationContainer<>(node.get(this.modelClass), this.loader, this.modelClass);
      } catch (final ConfigurateException exception) {
        throw new RuntimeException(exception);
      }
    });
  }

  /**
   * Creates and returns a new {@link JsonConfigurationContainer} from the provided file parameters.
   * <p>
   * The {@code loader} for the container will be configured to use a custom type-serializer implementation for {@link Component}s
   * serialization/deserialization support in configuration-models.
   *
   * @throws RuntimeException if an exception occurred during the process.
   * @param directory the directory of the file.
   * @param fileName the name of the file.
   * @param modelClass the model this file is represented by.
   * @param <C> a generic that indicates the class-type must extend from the {@link Configuration} contract.
   * @return the new {@link JsonConfigurationContainer}.
   * @since 1.0.0
   */
  public static <C extends Configuration> JsonConfigurationContainer<C> of(
      final Path directory,
      final String fileName,
      final Class<C> modelClass) {
    final Path path = directory.resolve(fileName + ConfigType.JSON.fileExtension());
    final GsonConfigurationLoader loader = GsonConfigurationLoader.builder()
        .indent(2)
        .lenient(true)
        .defaultOptions(opts -> Container.BUILT_IN_OPTIONS.apply(opts, null)) // JSON doesn't allow comments.
        .path(path)
        .build();
    try {
      final BasicConfigurationNode node = loader.load();
      final C config = node.get(modelClass);
      if (Files.notExists(path)) {
        node.set(modelClass, config);
        loader.save(node);
      }
      return new JsonConfigurationContainer<>(config, loader, modelClass);
    } catch (final ConfigurateException exception) {
      throw new RuntimeException(exception);
    }
  }
}
