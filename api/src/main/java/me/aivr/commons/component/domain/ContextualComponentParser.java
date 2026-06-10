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
package me.aivr.commons.component.domain;

import java.util.ArrayList;
import java.util.List;
import me.aivr.commons.component.domain.resolver.ContextualPlaceholderResolver;
import net.kyori.adventure.text.Component;

/**
 * Represents a {@link ComponentParser} type that allows to use context-based resolvable-placeholders for each parsed text.
 *
 * @param <P> the provider to use for actual component serialization/deserialization.
 * @since 2.4.0
 */
public interface ContextualComponentParser<P> extends ComponentParser<P> {
  /**
   * Returns the instance of the placeholder-resolver used by this parser.
   *
   * @return the {@link ContextualPlaceholderResolver}'s instance.
   * @since 2.4.0
   */
  ContextualPlaceholderResolver contextualPlaceholderResolver();

  /**
   * Parses the given text into a new {@link Component} list and resolves the placeholders based on the context-array given.
   *
   * @param inputs the inputs to parse.
   * @param contexts the contexts to use for placeholder-resolving.
   * @return a new list with the parsed components.
   * @since 2.4.0
   */
  @SuppressWarnings("unchecked")
  default <C> List<Component> parseBunchFromContexts(final List<String> inputs, final C... contexts) {
    final List<Component> parsedValues = new ArrayList<>(inputs.size());
    for (final String value : inputs) {
      parsedValues.add(this.parseSingleFromContexts(value, contexts));
    }
    return parsedValues;
  }

  /**
   * Parses the given text into a new {@link Component} and resolves the placeholders based on the context-array given.
   *
   * @param input the text to parse.
   * @param contexts the contexts to use for placeholder-resolving.
   * @return the parsed component.
   * @since 2.4.0
   */
  @SuppressWarnings("unchecked")
  <C> Component parseSingleFromContexts(final String input, final C... contexts);
}
