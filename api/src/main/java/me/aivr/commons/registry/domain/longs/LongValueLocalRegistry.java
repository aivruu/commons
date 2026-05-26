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

import it.unimi.dsi.fastutil.longs.LongPredicate;
import java.util.function.Predicate;
import me.aivr.commons.registry.domain.LocalRegistry;

/**
 * A type of {@link LocalRegistry} that uses {@code long} primitives as mapped-values for the registry's entries.
 *
 * @param <K> the type of key this registry uses.
 * @since 2.3.0
 */
public interface LongValueLocalRegistry<K> extends LocalRegistry<K, Long> {
  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #existsLongById(Object)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default boolean existsById(final K id) {
    return this.existsLongById(id);
  }

  /**
   * Checks whether there's an entry with the given id for this registry.
   *
   * @param id the entry's unique-identifier.
   * @return {@code true} if exists an entry for that ID, {@code false} otherwise.
   * @see #getLongById(Object) Entry's value retrieving by ID
   * @since 2.3.0
   */
  default boolean existsLongById(final K id) {
    return this.getLongById(id) != 0;
  }

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #getLongById(Object)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default Long findById(final K id) {
    return this.getLongById(id);
  }

  /**
   * Returns the {@code long} mapped to the given {@link K} key.
   *
   * @param id the entry's id.
   * @return the entry's mapped-value, or {@code null} if not exist.
   * @since 2.3.0
   */
  long getLongById(final K id);

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #registerLong(Object, long)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default Long register(final K id, final Long value) {
    return this.registerLong(id, value);
  }

  /**
   * Stores the given {@code long} with the specified ID into this registry, and returns whether the given value or the
   * old-mapping already existed for that identifier.
   *
   * @param id the id to assign.
   * @param value the value to store.
   * @return the stored value, or the entry's previous-mapping for the specified id, if existed.
   * @since 2.3.0
   */
  long registerLong(final K id, final long value);

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #unregisterLong(Object)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default Long unregister(final K id) {
    return this.unregisterLong(id);
  }

  /**
   * Removes the {@code long} for the given {@link K} id from this registry, and returns the value if available.
   *
   * @param id the id.
   * @return the removed value or {@code 0} if no mapping was assigned for that identifier.
   * @since 2.3.0
   */
  long unregisterLong(final K id);

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #unregisterLongIf(LongPredicate)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default int unregisterIf(final Predicate<Long> filter) {
    return this.unregisterLongIf((LongPredicate) filter);
  }

  /**
   * Removes the values from this registry that meets the {@code long} filter given for the function, and returns a count of how many
   * values were removed.
   *
   * @param filter the {@link LongPredicate} to use for removal.
   * @return the count of removed values.
   * @since 2.3.0
   */
  int unregisterLongIf(final LongPredicate filter);
}
