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

import me.aivr.commons.component.domain.resolver.context.ContextualPlaceholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.List;

/**
 * Represents a {@link PlaceholderResolver} for context-based ({@link ContextualPlaceholder}) placeholders.
 *
 * @since 2.4.0
 */
public interface ContextualPlaceholderResolver extends PlaceholderResolver {
  /**
   * A cached {@link TagResolver} used in functions that need to return a tag-resolver as a side-result.
   *
   * @since 2.4.0
   */
  TagResolver NO_PLACEHOLDER_TO_RESOLVE = TagResolver.empty();

  @Override
  @Deprecated
  default List<TagResolver> all() {
    throw new UnsupportedOperationException("This operation is not supported by this resolver.");
  }

  /**
   * Returns a {@link TagResolver} that represents a placeholder that was resolved from the given {@code context}.
   *
   * @param context the context given for the placeholder's resolution.
   * @return the resolved placeholder.
   * @since 2.4.0
   */
  <C> TagResolver resolveFromGivenContext(final C context);

  /**
   * Returns a {@link List} of {@link TagResolver}s that represents placeholders that were resolved from the given {@code contexts}.
   *
   * @param contexts a context "collection" given for the placeholders' resolution.
   * @return the resolved placeholders or {@link List#of()} if there are no placeholders registered.
   * @since 2.4.0
   */
  @SuppressWarnings("unchecked")
  <C> List<TagResolver> resolveFromMultipleContexts(final C... contexts);

  @Override
  @Deprecated
  default PlaceholderResolver registerSingle(final TagResolver placeholder) {
    throw new UnsupportedOperationException("This operation is not supported by this resolver.");
  }

  /**
   * Registers a bunch (array) of {@link ContextualPlaceholder}s for the given {@code contextType} parameter, and returns this
   * resolver's instance.
   *
   * @param contextType the type of context these placeholders will require.
   * @param placeholders the placeholders to register.
   * @return this resolver.
   * @since 2.4.0
   */
  @SuppressWarnings("unchecked")
  default <C> ContextualPlaceholderResolver registerBunchContextual(
      final Class<C> contextType,
      final ContextualPlaceholder<C>... placeholders) {
    for (final ContextualPlaceholder<C> placeholder : placeholders) this.registerSingleContextual(contextType, placeholder);
    return this;
  }

  /**
   * Register the given placeholder for the specified {@code contextType} and returns this resolver's instance.
   *
   * @param contextType the type of context the placeholder will require.
   * @param placeholder the placeholder to require.
   * @return this resolver.
   * @since 2.4.0
   */
  <C> ContextualPlaceholderResolver registerSingleContextual(final Class<C> contextType, final ContextualPlaceholder<C> placeholder);

  @Override
  @Deprecated
  default boolean unregisterSingle(final TagResolver placeholder) {
    throw new UnsupportedOperationException("This operation is not supported by this resolver.");
  }

  /**
   * Unregisters a bunch (array) of placeholders for the given context type, and returns this resolver's instance.
   *
   * @param contextTypes the context-types to be unregistered, and so its registered placeholders.
   * @return this resolver.
   * @since 2.4.0
   */
  default ContextualPlaceholderResolver unregisterBunchContextual(final Class<?>... contextTypes) {
    for (final Class<?> type : contextTypes) this.unregisterSingleContextual(type);
    return this;
  }

  /**
   * Unregisters the placeholders for the given context type.
   *
   * @param contextType the type of context to unregister and so its registered placeholders.
   * @return {@code true} if it was unregistered, {@code false} otherwise if it didn't exist.
   * @since 2.4.0
   */
  boolean unregisterSingleContextual(final Class<?> contextType);
}
