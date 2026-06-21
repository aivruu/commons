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
package me.aivr.commons.registry.domain.longs;

import it.unimi.dsi.fastutil.longs.LongSet;
import me.aivr.commons.registry.domain.LocalRegistry;
import org.jspecify.annotations.Nullable;

import java.util.Set;
import java.util.function.LongConsumer;

/**
 * A type of {@link LocalRegistry} that uses {@code long} primitives as keys for the registry's entries.
 *
 * @param <V> the type of value this registry handles.
 * @since 2.3.0
 */
public interface LongKeyLocalRegistry<V> extends LocalRegistry<Long, V> {
  /**
   * Reusable {@link Set} instance used by original deprecated-functions that handles wrapper-types instead of primitives.
   *
   * @since 2.3.0
   */
  Set<Long> CACHED_SET_FOR_DEPRECATED_FUNCTIONS = Set.of();

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #existsByLongId(long)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default boolean existsById(final Long id) {
    return this.existsByLongId(id);
  }

  /**
   * Checks whether there's an entry with the given long-id for this registry.
   *
   * @param id the long id.
   * @return {@code true} if exists an entry for that ID, {@code false} otherwise.
   * @see #getByLongId(long) Entry's value retrieving by {@code long} ID
   * @since 2.3.0
   */
  default boolean existsByLongId(final long id) {
    return this.getByLongId(id) != null;
  }

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #getByLongId(long)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default @Nullable V findById(final Long id) {
    return this.getByLongId(id);
  }

  /**
   * Returns, if available, the object mapped to the given {@code long} key.
   *
   * @param id the entry's id.
   * @return the entry's mapped-value, or {@code null} if not exist.
   * @since 2.3.0
   */
  @Nullable V getByLongId(final long id);

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #registerLong(long, Object)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default V register(final Long id, final V value) {
    return this.registerLong(id, value);
  }

  /**
   * Stores the given value with the specified {@code long} ID longo this registry, and returns whether the given value or the
   * old-mapping already existed for that identifier.
   *
   * @param id the id to assign.
   * @param value the value to store.
   * @return the stored value, or the entry's previous-mapping for the specified id, if existed.
   * @since 2.3.0
   */
  V registerLong(final long id, final V value);

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #findAllLongKeys()} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  @SuppressWarnings("unchecked")
  default <C extends Set<Long>> C findAllKeys() {
    return (C) CACHED_SET_FOR_DEPRECATED_FUNCTIONS;
  }

  /**
   * Returns a collection with this registry's all long keys, this function will not perform an action when a value is retrieved.
   *
   * @return the collection of long keys.
   * @since 2.3.0
   */
  default LongSet findAllLongKeys() {
    return this.findAllLongKeys(k -> {});
  }

  /**
   * Returns a collection with this registry's all long keys, and triggers an action by each retrieved value.
   *
   * @return the collection of long keys.
   * @since 2.3.0
   */
  LongSet findAllLongKeys(final LongConsumer postFetchAction);

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #unregisterLong(long)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default @Nullable V unregister(final Long id) {
    return this.unregisterLong(id);
  }

  /**
   * Removes the value for the specified identifier from this registry, and returns the value if existed.
   *
   * @param id the {@code long} id.
   * @return the removed value or {@code null} if no mapping was assigned for that identifier.
   * @since 2.3.0
   */
  @Nullable V unregisterLong(final long id);
}
