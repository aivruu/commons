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

import me.aivr.commons.component.domain.ContextualComponentParser;
import me.aivr.commons.component.domain.resolver.ContextualPlaceholderResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.List;

/**
 * Represents an implementation for {@link ContextualComponentParser} that also shares common-logic for contextual-parsers.
 *
 * @since 2.4.0
 */
public class ContextualComponentParserImpl extends ComponentParserImpl<MiniMessage> implements ContextualComponentParser<MiniMessage> {
  protected final ContextualPlaceholderResolver contextualResolver;

  /**
   * Creates a new {@link ContextualComponentParserImpl} with the provided parameters.
   *
   * @param parsingProvider the {@link MiniMessage} instance to use.
   * @param contextualResolver the {@link ContextualPlaceholderResolver}.
   * @since 2.4.0
   */
  public ContextualComponentParserImpl(final MiniMessage parsingProvider, final ContextualPlaceholderResolver contextualResolver) {
    super(parsingProvider);
    this.contextualResolver = contextualResolver;
  }

  @Override
  public ContextualPlaceholderResolver contextualPlaceholderResolver() {
    return this.contextualResolver;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <C> Component parseSingleFromContexts(final String input, final C... contexts) {
    if (contexts.length == 0) return Component.empty();

    return super.parsingProvider.deserialize(input, contexts.length == 1
        ? this.contextualResolver.resolveFromGivenContext(contexts[0]) : this.resolvePlaceholdersFromContextCollection(input, contexts));
  }

  /**
   * Resolves placeholders from the given contexts-array and combines them into a single {@link TagResolver}, if any.
   *
   * @param contexts an array with contexts for placeholders resolving.
   * @return the combined {@link TagResolver} or {@link ContextualPlaceholderResolver#NO_PLACEHOLDER_TO_RESOLVE} if there are no
   * placeholders to resolve.
   * @since 2.4.0
   */
  @SuppressWarnings("unchecked")
  protected <C> TagResolver resolvePlaceholdersFromContextCollection(final C... contexts)  {
    final List<TagResolver> resolvedPlaceholders = this.contextualResolver.resolveFromMultipleContexts(contexts);
    return resolvedPlaceholders.isEmpty()
        ? ContextualPlaceholderResolver.NO_PLACEHOLDER_TO_RESOLVE : TagResolver.resolver(resolvedPlaceholders);
  }
}
