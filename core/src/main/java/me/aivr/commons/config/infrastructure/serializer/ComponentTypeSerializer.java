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
package me.aivr.commons.config.infrastructure.serializer;

import java.lang.reflect.Type;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

/**
 * A {@link TypeSerializer} implementation that supports serialization/deserialization of {@link Component} types for
 * {@link me.aivr.commons.config.application.Configuration} models.
 *
 * @since 1.0.0
 */
@NullMarked
public enum ComponentTypeSerializer implements TypeSerializer<Component> {
  // lazy-singleton instance
  INSTANCE;

  /**
   * Used for actual components serialization and deserialization.
   *
   * @since 1.0.0
   */
  private static final MiniMessage COMPONENT_PARSER = MiniMessage.miniMessage();

  @Override
  public Component deserialize(final Type type, final ConfigurationNode node) {
    final String value = node.getString();
    return value == null ? Component.empty() : COMPONENT_PARSER.deserialize(value);
  }

  @Override
  public void serialize(final Type type, final @Nullable Component value, final ConfigurationNode node) throws SerializationException {
    if (value != null) node.set(String.class, COMPONENT_PARSER.serialize(value));
  }
}
