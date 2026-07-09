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

import me.aivr.commons.component.domain.resolver.StaticPlaceholderResolver;

/**
 * Represents a {@link ComponentParser} type that allows to use static-placeholders for each parsed text.
 *
 * @param <P> the provider to use for actual component serialization/deserialization.
 * @since 2.4.0
 */
public interface StaticResolvableComponentParser<P> extends ComponentParser<P> {
  /**
   * Returns the instance of the placeholder-resolver used by this parser.
   *
   * @return the {@link StaticPlaceholderResolver}'s instance.
   * @since 2.4.0
   */
  StaticPlaceholderResolver staticPlaceholderResolver();
}
