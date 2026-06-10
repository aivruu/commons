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

import me.aivr.commons.component.application.ContextualComponentParserImpl;
import me.aivr.commons.component.application.resolver.ContextualPlaceholderResolverImpl;
import me.aivr.commons.component.application.resolver.StaticPlaceholderResolverImpl;
import me.aivr.commons.component.domain.resolver.ContextualPlaceholderResolver;
import me.aivr.commons.component.domain.resolver.StaticPlaceholderResolver;
import me.aivr.commons.component.domain.type.MiniMessageParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

/**
 * Represents an implementation for the {@link MiniMessageParser}.
 *
 * @since 2.4.0
 */
public final class MiniMessageParserImpl extends ContextualComponentParserImpl implements MiniMessageParser {
  /**
   * A custom {@link MiniMessage} instance that disables italic {@code <i>} decoration if it's not present.
   *
   * @since 2.4.0
   */
  private static final MiniMessage NO_ITALICS_INSTANCE = MiniMessage.builder()
      .postProcessor(component -> component.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE))
      .build();
  private final StaticPlaceholderResolver staticResolver;

  /**
   * Creates a new {@link MiniMessageParserImpl} instance, this object will use this parser's {@link #NO_ITALICS_INSTANCE}, as well
   * the default {@link ContextualPlaceholderResolver} and {@link StaticPlaceholderResolver} implementations.
   *
   * @see #MiniMessageParserImpl(MiniMessage, ContextualPlaceholderResolver, StaticPlaceholderResolver) Custom-implementations parser
   * construction
   * @since 2.4.0
   */
  public MiniMessageParserImpl() {
    this(NO_ITALICS_INSTANCE, new ContextualPlaceholderResolverImpl(), new StaticPlaceholderResolverImpl());
  }

  /**
   * Creates a new {@link MiniMessageParserImpl} with the provided parameters.
   *
   * @param parsingProvider the {@link MiniMessage} instance to use.
   * @param contextualResolver the {@link ContextualPlaceholderResolver}.
   * @param staticResolver the {@link StaticPlaceholderResolver}.
   * @since 2.4.0
   */
  public MiniMessageParserImpl(
      final MiniMessage parsingProvider,
      final ContextualPlaceholderResolver contextualResolver,
      final StaticPlaceholderResolver staticResolver) {
    super(parsingProvider, contextualResolver);
    this.staticResolver = staticResolver;
  }

  @Override
  public StaticPlaceholderResolver staticPlaceholderResolver() {
    return this.staticResolver;
  }

  @Override
  public Component parseSingle(final String value) {
    final TagResolver staticPlaceholders = this.staticResolver.resolvedPlaceholders();
    return staticPlaceholders == TagResolver.empty()
        ? super.parseSingle(value) : super.parsingProvider.deserialize(value, TagResolver.resolver(staticPlaceholders));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <C> Component parseSingleFromContexts(final String input, final C... contexts) {
    final boolean hasStaticPlaceholders = this.staticResolver.any();
    final boolean hasContextualPlaceholders = contexts.length == 0 || super.contextualResolver.any();
    if (!hasStaticPlaceholders && !hasContextualPlaceholders) {
      return super.parseSingle(input);
    }
    final TagResolver resolvedStaticPlaceholders = this.staticResolver.resolvedPlaceholders();
    if (hasStaticPlaceholders && !hasContextualPlaceholders) {
      return super.parsingProvider.deserialize(input, resolvedStaticPlaceholders);
    }
    return !hasStaticPlaceholders
        ? super.parseSingleFromContexts(input, contexts)
        : super.parsingProvider.deserialize(input, resolvedStaticPlaceholders, super.resolvePlaceholdersFromContextCollection(contexts));
  }
}
