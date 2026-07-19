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
package me.aivr.commons.registry.domain;

import it.unimi.dsi.fastutil.objects.Object2ObjectFunction;

/**
 * A type of {@link LocalRegistry} that allows to use generic-types for both key and value.
 * <p>
 * This interface uses the {@link Object2ObjectFunction} as the type for mapping-related operations for
 * {@link LocalRegistry}.
 *
 * @param <K> the type of key this registry uses.
 * @param <V> the type of value this registry handles.
 * @since 3.1.0
 */
public interface GenericTypeRegistry<K, V> extends LocalRegistry<K, V, Object2ObjectFunction<K, V>> {}
