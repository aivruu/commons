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
package me.aivr.commons.component.application.resolver;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import me.aivr.commons.component.domain.resolver.ContextualPlaceholderResolver;
import me.aivr.commons.component.domain.resolver.context.ContextualPlaceholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

/**
 * Represents a simple-implementation for {@link ContextualPlaceholderResolver}.
 *
 * @since 2.4.0
 */
public final class ContextualPlaceholderResolverImpl implements ContextualPlaceholderResolver {
  private final Object2ObjectMap<Class<?>, ContextualPlaceholder<?>> contextualPlaceholders = new Object2ObjectOpenHashMap<>();

  @Override
  @SuppressWarnings("unchecked")
  public <C> TagResolver resolveFromGivenContext(final C context) {
    final ContextualPlaceholder<C> placeholder = (ContextualPlaceholder<C>) this.contextualPlaceholders.get(context.getClass());
    return placeholder == null ? ContextualPlaceholderResolver.NO_PLACEHOLDER_TO_RESOLVE : placeholder.apply(context);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <C> List<TagResolver> resolveFromMultipleContexts(final C... contexts) {
    if (contexts.length == 0 || this.contextualPlaceholders.isEmpty()) return List.of();

    final List<TagResolver> resolvedPlaceholders = new ArrayList<>(this.contextualPlaceholders.size());
    TagResolver resolved;
    for (final C context : contexts) {
      resolved = this.resolveFromGivenContext(context);
      if (resolved == ContextualPlaceholderResolver.NO_PLACEHOLDER_TO_RESOLVE) {
        continue;
      }
      resolvedPlaceholders.add(resolved);
    }
    return resolvedPlaceholders;
  }

  @Override
  public boolean any() {
    return !this.contextualPlaceholders.isEmpty();
  }

  @Override
  public <C> ContextualPlaceholderResolver registerSingleContextual(final Class<C> contextType, final ContextualPlaceholder<C> placeholder) {
    this.contextualPlaceholders.put(contextType, placeholder);
    return this;
  }

  @Override
  public boolean unregisterSingleContextual(final Class<?> contextType) {
    return this.contextualPlaceholders.remove(contextType) != null;
  }

  @Override
  public void unregisterAll() {
    this.contextualPlaceholders.clear();
  }
}
