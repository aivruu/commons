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
package me.aivr.commons.registry.domain.ints;

import it.unimi.dsi.fastutil.ints.IntSet;
import me.aivr.commons.registry.domain.LocalRegistry;
import org.jspecify.annotations.Nullable;

import java.util.Set;
import java.util.function.IntConsumer;

/**
 * A type of {@link LocalRegistry} that uses {@code int} primitives as keys for the registry's entries.
 *
 * @param <V> the type of value this registry handles.
 * @since 2.3.0
 */
public interface IntKeyLocalRegistry<V> extends LocalRegistry<Integer, V> {
  /**
   * Reusable {@link Set} instance used by original deprecated-functions that handles wrapper-types instead of primitives.
   *
   * @since 2.3.0
   */
  Set<Integer> CACHED_SET_FOR_DEPRECATED_FUNCTIONS = Set.of();

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #existsByIntId(int)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default boolean existsById(final Integer id) {
    return this.existsByIntId(id);
  }

  /**
   * Checks whether there's an entry with the given int-id for this registry.
   *
   * @param id the int id.
   * @return {@code true} if exists an entry for that ID, {@code false} otherwise.
   * @see #getByIntId(int) Entry's value retrieving by {@code int} ID
   * @since 2.3.0
   */
  default boolean existsByIntId(final int id) {
    return this.getByIntId(id) != null;
  }

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #getByIntId(int)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default @Nullable V findById(final Integer id) {
    return this.getByIntId(id);
  }

  /**
   * Returns, if available, the object mapped to the given {@code int} key.
   *
   * @param id the entry's id.
   * @return the entry's mapped-value, or {@code null} if not exist.
   * @since 2.3.0
   */
  @Nullable V getByIntId(final int id);

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #registerInt(int, Object)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default V register(final Integer id, final V value) {
    return this.registerInt(id, value);
  }

  /**
   * Stores the given value with the specified {@code int} ID into this registry, and returns whether the given value or the
   * old-mapping already existed for that identifier.
   *
   * @param id the id to assign.
   * @param value the value to store.
   * @return the stored value, or the entry's previous-mapping for the specified id, if existed.
   * @since 2.3.0
   */
  V registerInt(final int id, final V value);

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #findAllIntKeys()} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  @SuppressWarnings("unchecked")
  default <C extends Set<Integer>> C findAllKeys() {
    return (C) CACHED_SET_FOR_DEPRECATED_FUNCTIONS;
  }

  /**
   * Returns a collection with this registry's all int keys, this function will not perform an action when a value is retrieved.
   *
   * @return the collection of int keys.
   * @since 2.3.0
   */
  default IntSet findAllIntKeys() {
    return this.findAllIntKeys(k -> {});
  }

  /**
   * Returns a collection with this registry's all int keys, and triggers an action by each retrieved value.
   *
   * @return the collection of int keys.
   * @since 2.3.0
   */
  IntSet findAllIntKeys(final IntConsumer postFetchAction);

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #unregisterInt(int)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default @Nullable V unregister(final Integer id) {
    return this.unregisterInt(id);
  }

  /**
   * Removes the value for the specified identifier from this registry, and returns the value if existed.
   *
   * @param id the {@code int} id.
   * @return the removed value or {@code null} if no mapping was assigned for that identifier.
   * @since 2.3.0
   */
  @Nullable V unregisterInt(final int id);
}
