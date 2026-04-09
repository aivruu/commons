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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import me.aivr.commons.config.application.Configuration;
import me.aivr.commons.config.infrastructure.serializer.ComponentTypeSerializer;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

/**
 * Represents a container for internal configuration-related data.
 *
 * @param model the model used for configuration-access.
 * @param loader the loader for the model.
 * @param modelClass the class-type of the model.
 * @param <Config> a generic that indicates the model or class-type must extend from the {@link Configuration} contract.
 * @since 1.0.0
 */
@NullMarked
record ConfigurationContainer<Config extends Configuration>(Config model, HoconConfigurationLoader loader, Class<Config> modelClass) {
  /**
   * The header to apply for the file this container's model represents.
   *
   * @since 1.0.0
   */
  static final String HEADER = "?";

  /**
   * Reloads the configuration contents asynchronously and returns a new container as result.
   *
   * @throws RuntimeException if an exception occurred during the process.
   * @return a {@link CompletableFuture} that contains the {@link ConfigurationContainer} returned by the operation.
   * @since 1.0.0
   */
  CompletableFuture<ConfigurationContainer<Config>> reloadAsync() {
    return CompletableFuture.supplyAsync(() -> {
      try {
        final CommentedConfigurationNode node = this.loader.load();
        return new ConfigurationContainer<>(node.get(this.modelClass), this.loader, this.modelClass);
      } catch (final ConfigurateException exception) {
        throw new RuntimeException(exception);
      }
    });
  }

  /**
   * Creates and returns a new {@link ConfigurationContainer} from the provided file parameters.
   * <p>
   * The {@code loader} for the container will be configured to use the {@link #HEADER} provided by this class, as well the
   * {@link ComponentTypeSerializer} implementation used for component-types support in configuration-models.
   *
   * @throws RuntimeException if an exception occurred during the process.
   * @param directory the directory of the file.
   * @param fileName the name of the file.
   * @param modelClass the model this file is represented by.
   * @param <C> a generic that indicates the class-type must extend from the {@link Configuration} contract.
   * @return the new {@link ConfigurationContainer}.
   * @since 1.0.0
   */
  static <C extends Configuration> ConfigurationContainer<C> of(final Path directory, final String fileName, final Class<C> modelClass) {
    final Path path = directory.resolve(fileName + ".yml");
    final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
        .indent(2)
        .defaultOptions(opts -> opts
            .header(HEADER)
            .shouldCopyDefaults(true)
            .serializers(builder -> builder.register(Component.class, ComponentTypeSerializer.INSTANCE))
        )
        .path(path)
        .build();
    try {
      final CommentedConfigurationNode node = loader.load();
      final C config = node.get(modelClass);
      if (Files.notExists(path)) {
        node.set(modelClass, config);
        loader.save(node);
      }
      return new ConfigurationContainer<>(config, loader, modelClass);
    } catch (final ConfigurateException exception) {
      throw new RuntimeException(exception);
    }
  }
}
