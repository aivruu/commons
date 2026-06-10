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
package me.aivr.commons.aggregate.infrastructure.repository.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.aivr.commons.aggregate.infrastructure.repository.json.codec.JsonCodec;
import org.jspecify.annotations.Nullable;

/**
 * A provider of configurable {@link Gson} instances that can be used for a {@link JsonAsyncAggregateRootRepository}.
 *
 * @since 1.0.0
 */
public final class GsonProvider {
  private static @Nullable Gson gson;

  private GsonProvider() {
    throw new UnsupportedOperationException("This class is for utility.");
  }

  /**
   * Returns the gson-instance if available.
   *
   * @throws IllegalStateException if no instance has been configured for the provider.
   * @return the {@link Gson} instance.
   * @since 1.0.0
   */
  public static Gson get() {
    if (gson == null) throw new IllegalStateException("JSON-provider has not been initialized yet.");
    return gson;
  }

  /**
   * Configures a {@link Gson} instance with the custom {@code codecs} provided for the method.
   *
   * @param codecs the {@link JsonCodec}s to include within the {@link Gson} instance.
   * @since 1.0.0
   */
  public static void build(final JsonCodec<?>... codecs) {
    if (gson != null) return;

    final GsonBuilder builder = new GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls();
    for (final JsonCodec<?> codec : codecs) {
      builder.registerTypeAdapter(codec.type(), codec);
    }
    gson = builder.create();
  }
}
