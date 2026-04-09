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
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class GsonProvider {
  private static @Nullable Gson gson;

  private GsonProvider() {
    throw new UnsupportedOperationException("This class is for utility.");
  }

  public static Gson get() {
    if (gson == null) throw new IllegalStateException("JSON-provider has not been initialized yet.");
    return gson;
  }

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
