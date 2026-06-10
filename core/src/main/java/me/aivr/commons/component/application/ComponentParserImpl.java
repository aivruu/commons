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
package me.aivr.commons.component.application;

import me.aivr.commons.component.domain.ComponentParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

/**
 * Represents a common-implementation for {@link ComponentParser} that also shares common-logic between parsers.
 *
 * @param <P> represents a component-serializer (e.g. {@link net.kyori.adventure.text.minimessage.MiniMessage}).
 * @since 2.4.0
 */
public class ComponentParserImpl<P extends ComponentSerializer<Component, ? extends Component, String>>
    implements ComponentParser<P> {
  protected final P parsingProvider;

  /**
   * Creates a new {@link ComponentParserImpl} with the provided parameter.
   *
   * @param parsingProvider the provider to use for component serialization/deserialization.
   * @since 2.4.0
   */
  public ComponentParserImpl(final P parsingProvider) {
    this.parsingProvider = parsingProvider;
  }

  @Override
  public P providerInstance() {
    return this.parsingProvider;
  }

  @Override
  public Component parseSingle(final String value) {
    return this.parsingProvider.deserialize(value);
  }

  @Override
  public String unparseSingle(final Component value) {
    return this.parsingProvider.serialize(value);
  }
}
