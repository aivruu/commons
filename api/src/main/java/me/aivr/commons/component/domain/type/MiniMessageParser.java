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
package me.aivr.commons.component.domain.type;

import me.aivr.commons.component.domain.ContextualComponentParser;
import me.aivr.commons.component.domain.StaticResolvableComponentParser;
import net.kyori.adventure.text.minimessage.MiniMessage;

/**
 * Represents a component-parser subtype that uses the {@link MiniMessage} provider for
 * {@link net.kyori.adventure.text.Component} serialization/deserialization, and allows to resolve static and context-based placeholders
 * for parsed components.
 *
 * @since 2.4.0
 */
public interface MiniMessageParser extends StaticResolvableComponentParser<MiniMessage>, ContextualComponentParser<MiniMessage> {}
