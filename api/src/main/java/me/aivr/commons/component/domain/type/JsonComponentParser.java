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

import me.aivr.commons.component.domain.ComponentParser;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;

/**
 * Represents a {@link ComponentParser} that uses the {@link JSONComponentSerializer} provider for
 * {@link net.kyori.adventure.text.Component} serialization/deserialization.
 *
 * @since 2.4.0
 */
public interface JsonComponentParser extends ComponentParser<JSONComponentSerializer> {}
