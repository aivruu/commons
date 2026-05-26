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

import it.unimi.dsi.fastutil.ints.IntPredicate;
import java.util.function.Predicate;
import me.aivr.commons.registry.domain.LocalRegistry;

/**
 * A type of {@link LocalRegistry} that uses {@code int} primitives as mapped-values for the registry's entries.
 *
 * @param <K> the type of key this registry uses.
 * @since 2.3.0
 */
public interface IntValueLocalRegistry<K> extends LocalRegistry<K, Integer> {
  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #existsIntById(Object)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default boolean existsById(final K id) {
    return this.existsIntById(id);
  }

  /**
   * Checks whether there's an entry with the given id for this registry.
   *
   * @param id the entry's unique-identifier.
   * @return {@code true} if exists an entry for that ID, {@code false} otherwise.
   * @see #getIntById(Object) Entry's value retrieving by ID
   * @since 2.3.0
   */
  default boolean existsIntById(final K id) {
    return this.getIntById(id) != 0;
  }

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #getIntById(Object)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default Integer findById(final K id) {
    return this.getIntById(id);
  }

  /**
   * Returns the {@code int} mapped to the given {@link K} key.
   *
   * @param id the entry's id.
   * @return the entry's mapped-value, or {@code 0} if not exist.
   * @since 2.3.0
   */
  int getIntById(final K id);

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #registerInt(Object, int)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default Integer register(final K id, final Integer value) {
    return this.registerInt(id, value);
  }

  /**
   * Stores the given {@code int} with the specified ID into this registry, and returns whether the given value or the
   * old-mapping already existed for that identifier.
   *
   * @param id the id to assign.
   * @param value the value to store.
   * @return the stored value, or the entry's previous-mapping for the specified id, if existed.
   * @since 2.3.0
   */
  int registerInt(final K id, final int value);

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #unregisterInt(Object)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default Integer unregister(final K id) {
    return this.unregisterInt(id);
  }

  /**
   * Removes the {@code int} for the given {@link K} id from this registry, and returns the value if available.
   *
   * @param id the id.
   * @return the removed value or {@code 0} if no mapping was assigned for that identifier.
   * @since 2.3.0
   */
  int unregisterInt(final K id);

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #unregisterIntIf(IntPredicate)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default int unregisterIf(final Predicate<Integer> filter) {
    return this.unregisterIntIf((IntPredicate) filter);
  }

  /**
   * Removes the values from this registry that meets the {@code int} filter given for the function, and returns a count of how many
   * values were removed.
   *
   * @param filter the {@link IntPredicate} to use for removal.
   * @return the count of removed values.
   * @since 2.3.0
   */
  int unregisterIntIf(final IntPredicate filter);
}
