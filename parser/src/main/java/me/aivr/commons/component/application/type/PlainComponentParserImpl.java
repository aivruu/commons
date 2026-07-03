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
import me.aivr.commons.component.domain.type.PlainComponentParser;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

/**
 * Represents an implementation for the {@link PlainComponentParser}.
 *
 * @since 2.4.0
 */
public final class PlainComponentParserImpl extends ComponentParserImpl<PlainTextComponentSerializer> implements PlainComponentParser {
  /**
   * Creates a new {@link PlainComponentParser} instance, this object will use the default {@link PlainTextComponentSerializer}
   * instance provided by the {@link PlainTextComponentSerializer#plainText()} function.
   *
   * @see #PlainComponentParserImpl(PlainTextComponentSerializer) Custom-serializer parser construction
   * @since 2.4.0
   */
  public PlainComponentParserImpl() {
    this(PlainTextComponentSerializer.plainText());
  }

  /**
   * Creates a new {@link PlainComponentParser} with the provided parameter.
   *
   * @param serializer the {@link PlainTextComponentSerializer} to use for this parser.
   * @since 2.4.0
   */
  public PlainComponentParserImpl(final PlainTextComponentSerializer serializer) {
    super(serializer);
  }

}
