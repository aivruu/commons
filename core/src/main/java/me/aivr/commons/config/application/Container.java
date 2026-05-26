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
package me.aivr.commons.config.application;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import me.aivr.commons.config.infrastructure.serializer.ComponentTypeSerializer;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationOptions;

/**
 * Represents a container for internal configuration-related information.
 *
 * @param <Config> a model that represents a configuration-file.
 * @since 2.3.0
 */
public interface Container<Config extends Configuration> {
  /**
   * The default {@link ConfigurationOptions} to use for every configuration file.
   *
   * @since 2.3.0
   */
  BiFunction<ConfigurationOptions, @Nullable String, ConfigurationOptions> BUILT_IN_OPTIONS = (opts, header) -> opts
      .header(header)
      .serializers(builder -> builder.register(Component.class, ComponentTypeSerializer.INSTANCE))
      .shouldCopyDefaults(true);

  /**
   * Returns this container's configuration-model, if available.
   *
   * @return This container's {@link Config}.
   * @since 2.3.0
   */
  Config model();

  /**
   * Returns the header set for this container's configuration-file, if configured.
   *
   * @return the configuration header, or {@code null} if none was configured.
   * @since 2.3.0
   */
  @Nullable String header();

  /**
   * Returns the class-type of the configuration-model this container represents to.
   *
   * @return the configuration-model's class-type.
   * @since 2.3.0
   */
  Class<Config> modelClass();

  /**
   * Reloads this container's configuration-model and returns a new {@link Container} with the updated model.
   *
   * @return a {@link CompletableFuture} that contains the actual result of the operation.
   * @since 2.3.0
   */
  CompletableFuture<Container<Config>> reload();
}
