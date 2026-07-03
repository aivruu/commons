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
package me.aivr.commons.component.domain.resolver;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.List;

/**
 * Represents a container for resolved placeholders.
 *
 * @since 2.4.0
 */
public interface PlaceholderResolver {
  /**
   * Returns a list with all the placeholders existing.
   *
   * @return a {@link List} of {@link TagResolver}.
   * @since 2.4.0
   */
  List<TagResolver> all();

  /**
   * Checks if there are placeholders stored within this resolver.
   *
   * @return {@code true} if there are placeholders, {@code false} otherwise.
   * @since 2.4.0
   */
  boolean any();

  Component apply(final Component component);

  /**
   * Registers a bunch (array) of placeholders and returns this resolver's instance.
   *
   * @param placeholders the placeholders to be registered.
   * @return this resolver.
   * @see #registerSingle(TagResolver) Single placeholder registration
   * @since 2.4.0
   */
  default PlaceholderResolver registerBunch(final TagResolver... placeholders) {
    for (final TagResolver placeholder : placeholders) this.registerSingle(placeholder);
    return this;
  }

  /**
   * Registers the given placeholder and returns this resolver's instance.
   *
   * @param placeholder the placeholder to be registered.
   * @return this resolver.
   * @since 2.4.0
   */
  PlaceholderResolver registerSingle(final TagResolver placeholder);

  /**
   * Unregisters a bunch (array) of placeholders and returns this resolver's instance.
   *
   * @param placeholders the placeholders to unregister.
   * @see #unregisterSingle(TagResolver) Single placeholder unregistration
   * @since 2.4.0
   */
  default void unregisterBunch(final TagResolver... placeholders) {
    for (final TagResolver placeholder : placeholders) this.unregisterSingle(placeholder);
  }

  /**
   * Unregisters the given placeholder and returns a result for the operation.
   *
   * @param placeholder the placeholder to unregister.
   * @return {@code true} if the placeholder was unregistered, {@code false} otherwise.
   * @since 2.4.0
   */
  boolean unregisterSingle(final TagResolver placeholder);

  /**
   * Unregisters all the placeholders for this resolver.
   *
   * @since 2.4.0
   */
  void unregisterAll();
}
