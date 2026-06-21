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

import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a parser that allows to convert from raw-text (String) into modern Adventure's {@link Component}s and vice versa.
 *
 * @param <P> the provider to use for actual component serialization/deserialization.
 * @since 2.4.0
 */
public interface ComponentParser<P> {
  /**
   * Returns the instance of the provider that's being used.
   *
   * @return the provider's instance.
   * @since 2.4.0
   */
  P providerInstance();

  /**
   * Parses the given text into a new {@link Component}.
   *
   * @param value the value to parse.
   * @return the parsed component.
   * @since 2.4.0
   */
  Component parseSingle(final String value);

  /**
   * Parses the given string-array into a new {@link Component} array.
   *
   * @param values the values to parse.
   * @return the array with the parsed values.
   * @see #parseBunch(List) As list parsing for values
   * @since 2.4.0
   */
  default Component[] parseBunch(final String... values) {
    return this.parseBunch(List.of(values)).toArray(Component[]::new);
  }

  /**
   * Parses the given string-list into a new {@link Component} list.
   *
   * @param values the values to parse.
   * @return the list with the parsed values.
   * @see #parseSingle(String) Parse value as a single one
   * @since 2.4.0
   */
  default List<Component> parseBunch(final List<String> values) {
    final List<Component> parsedValues = new ArrayList<>(values.size());
    for (final String value : values) {
      parsedValues.add(this.parseSingle(value));
    }
    return parsedValues;
  }

  /**
   * Converts the component to its string form.
   *
   * @param value the component to convert.
   * @return the unparsed component.
   * @since 2.4.0
   */
  String unparseSingle(final Component value);

  /**
   * Parses the given component-array into a new String array.
   *
   * @param values the components to convert.
   * @return the array with the converted values.
   * @see #unparseBunch(List) As list unparsing for components
   * @since 2.4.0
   */
  default String[] unparseBunch(final Component... values) {
    return this.unparseBunch(List.of(values)).toArray(String[]::new);
  }

  /**
   * Converts the given component-list into a new String list.
   *
   * @param values the components to convert.
   * @return the list with the converted values.
   * @see #unparseSingle(Component) Unparse value as a single one
   * @since 2.4.0
   */
  default List<String> unparseBunch(final List<Component> values) {
    final List<String> serializedValues = new ArrayList<>(values.size());
    for (final Component component : values) {
      serializedValues.add(this.unparseSingle(component));
    }
    return serializedValues;
  }
}
