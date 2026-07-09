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

import me.aivr.commons.registry.domain.util.GenericRegistryVisitor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Represents a local-registry used to handle contained information that's required to be accessed quickly.
 *
 * @param <K> the type of id this registry handles.
 * @param <V> the type of value this registry handles.
 * @since 2.3.0
 */
public interface LocalRegistry<K, V> {
  /**
   * Checks whether there's an entry with the given identifier for this registry.
   *
   * @param id the entry's unique-identifier.
   * @return {@code true} if exists an entry for that ID, {@code false} otherwise.
   * @see #findById(Object) Entry's value retrieving by ID
   * @since 2.3.0
   */
  default boolean existsById(final K id) {
    return this.findById(id) != null;
  }

  /**
   * Returns the value, if available, of the entry for the specified identifier.
   *
   * @param id the entry's unique-identifier.
   * @return the entry's value or {@code null} if there's none for the entry.
   * @since 2.3.0
   */
  @Nullable V findById(final K id);

  /**
   * Returns a collection with this registry's all keys, this function will not perform an action when a value is retrieved.
   *
   * @param <C> represents a collection, or similar, of {@link K} objects.
   * @return the collection of keys.
   * @since 2.3.0
   */
  default <C extends Set<K>> C findAllKeys() {
    return this.findAllKeys(k -> {});
  }

  /**
   * Returns a collection with this registry's all keys, and triggers an action by each retrieved value.
   *
   * @param postFetchAction the action to execute for each key.
   * @param <C> represents a collection, or similar, of {@link K} objects.
   * @return the collection of keys.
   * @since 3.0.0-rc2
   */
  @SuppressWarnings("unchecked")
  default <C extends Set<K>> C findAllKeys(final Consumer<K> postFetchAction) {
    return (C) GenericRegistryVisitor.visitKeys(this.raw().keySet(), postFetchAction);
  }

  /**
   * Returns a collection with this registry's all values, this function will not perform an action when a value is retrieved.
   *
   * @param <C> represents a collection, or similar, of {@link V} objects.
   * @return the collection of values.
   * @see #findAllValues(Consumer) Actual values-collection retrieving
   * @since 2.3.0
   */
  default <C extends Collection<V>> C findAllValues() {
    return this.findAllValues(v -> {});
  }

  /**
   * Returns a collection with this registry's all values, and triggers an action by each retrieved value.
   *
   * @param postFetchAction the action to execute for each value.
   * @param <C> represents a collection, or similar, of {@link V} objects.
   * @return the collection of values.
   * @since 3.0.0-rc2
   */
  @SuppressWarnings("unchecked")
  default <C extends Collection<V>> C findAllValues(final Consumer<V> postFetchAction) {
    return (C) GenericRegistryVisitor.visitValues(this.raw().values(), postFetchAction);
  }

  /**
   * Returns a collection with values from this registry that meets the condition given.
   *
   * @param condition the condition used to filter by the registry's values.
   * @param <C> represents a collection, or similar, of {@link V} objects.
   * @return the collection of values.
   * @since 3.0.0-rc2
   */
  @SuppressWarnings("unchecked")
  default <C extends Collection<V>> C filter(final Predicate<V> condition) {
    return (C) GenericRegistryVisitor.filter(this.raw().values(), condition);
  }

  /**
   * Stores the given value with the specified ID into this registry, and returns whether the given value or the
   * old-mapping already existed for that identifier.
   *
   * @param id the unique-identifier to assign.
   * @param value the value to store.
   * @return the stored value, or the entry's previous-mapping for the specified id, if existed.
   * @since 2.3.0
   */
  V register(final K id, final V value);

  /**
   * Removes the value for the specified identifier from this registry, and returns the value if existed.
   *
   * @param id the unique-identifier.
   * @return the removed value or {@code null} if no mapping was assigned for that identifier.
   * @since 2.3.0
   */
  @Nullable V unregister(final K id);

  /**
   * Removes the values from this registry that meets the filter given for the function, and returns a count of how many
   * values were removed.
   *
   * @param filter the filter to use for value-removal.
   * @return the count of removed values.
   * @since 2.3.0
   */
  int unregisterIf(final Predicate<V> filter);

  /**
   * Removes all the values stored by this registry.
   *
   * @see Map#clear()
   * @since 3.0.0-rc2
   */
  default void unregisterAll() {
    this.raw().clear();
  }

  /**
   * Returns the internal-container for this registry's information.
   *
   * @implNote This function's return depends on the implementation-type used to call this function, most of the
   * implementations may opt by return a generic-type implementation, or, if the registry handles a type-specific
   * value (e.g. a primitive, like {@link me.aivr.commons.registry.domain.ints.IntKeyLocalRegistry}), it may return
   * a fast-util implementation instead.
   * @return the {@link Map} implementation used for this registry.
   * @since 3.0.0
   */
  @ApiStatus.Experimental
  <M extends Map<K, V>> M raw();
}
