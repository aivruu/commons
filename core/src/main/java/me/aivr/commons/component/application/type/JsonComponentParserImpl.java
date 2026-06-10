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
package me.aivr.commons.component.application.type;

import me.aivr.commons.component.application.ComponentParserImpl;
import me.aivr.commons.component.domain.type.JsonComponentParser;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;

/**
 * Represents an implementation for the {@link JsonComponentParser}.
 *
 * @since 2.4.0
 */
public final class JsonComponentParserImpl extends ComponentParserImpl<JSONComponentSerializer> implements JsonComponentParser {
  /**
   * Creates a new {@link JsonComponentParserImpl} instance, this object will use the default {@link JSONComponentSerializer}
   * instance provided by the {@link JSONComponentSerializer#json()} function.
   *
   * @see JsonComponentParserImpl#JsonComponentParserImpl(JSONComponentSerializer) Custom-serializer parser construction
   * @since 2.4.0
   */
  public JsonComponentParserImpl() {
    super(JSONComponentSerializer.json());
  }

  /**
   * Creates a new {@link JsonComponentParserImpl} with the provided parameter.
   *
   * @param serializer the {@link JSONComponentSerializer} to use for this parser.
   * @since 2.4.0
   */
  public JsonComponentParserImpl(final JSONComponentSerializer serializer) {
    super(serializer);
  }
}
