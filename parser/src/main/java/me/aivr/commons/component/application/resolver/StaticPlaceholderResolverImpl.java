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

import me.aivr.commons.component.domain.resolver.StaticPlaceholderResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a simple-implementation for {@link StaticPlaceholderResolver}.
 *
 * @since 2.4.0
 */
public class StaticPlaceholderResolverImpl implements StaticPlaceholderResolver {
  protected final List<TagResolver> staticPlaceholders = new ArrayList<>();
  protected @Nullable TagResolver resolvedPlaceholders;

  @Override
  public TagResolver resolvedPlaceholders() {
    return this.resolvedPlaceholders == null ? TagResolver.empty() : this.resolvedPlaceholders;
  }

  @Override
  public boolean any() {
    return !this.staticPlaceholders.isEmpty();
  }

  @Override
  public Component apply(final Component component) {
    return component.replaceText(builder -> {});
  }

  @Override
  public StaticPlaceholderResolver registerSingle(final TagResolver placeholder) {
    this.staticPlaceholders.add(placeholder);
    this.updateCollection();
    return this;
  }

  protected void updateCollection() { // why did I call it that way?
    this.resolvedPlaceholders = TagResolver.resolver(this.staticPlaceholders);
  }

  @Override
  public boolean unregisterSingle(final TagResolver placeholder) {
    final boolean removed = this.staticPlaceholders.remove(placeholder);
    if (removed) {
      this.updateCollection();
    }
    return removed;
  }

  @Override
  public void unregisterAll() {
    this.staticPlaceholders.clear();
    this.resolvedPlaceholders = null;
  }
}
