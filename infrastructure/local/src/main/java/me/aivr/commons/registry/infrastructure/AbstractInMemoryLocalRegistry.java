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
package me.aivr.commons.registry.infrastructure;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import me.aivr.commons.registry.domain.LocalRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A class that abstracts common-logic related to registries and serves for registry-implementations that handle expirable and
 * non-expirable entries.
 *
 * @param <K> the type of id this registry uses.
 * @param <V> the type of value this registry handles.
 * @param <M> the map-implementation this registry uses.
 * @since 2.3.0
 */
public abstract class AbstractInMemoryLocalRegistry<K, V, M extends Map<K, V>> implements LocalRegistry<K, V> {
  /**
   * The {@link Map} type to use as cache-handler for this registry.
   *
   * @since 2.3.0
   */
  protected final M cache;

  protected AbstractInMemoryLocalRegistry(final M cache) {
    this.cache = cache;
  }

  /**
   * {@inheritDoc}
   * <p>
   * This function will create a new modifiable-set for all this registry's keys with a size resolvable as this registry's
   * count of stored-entries as of now, also known as -> {@code this.cache.keySet().size()}.
   * <p>
   * Modifications performed over the returned collection won't reflect any changes on the original collection.
   *
   * @since 2.3.0
   */
  @Override
  @SuppressWarnings("unchecked")
  public final <C extends Set<K>> C findAllKeys(final Consumer<K> postFetchAction) {
    final C registryKeys = (C) this.cache.keySet();
    final C keys = (C) new ObjectOpenHashSet<>(registryKeys.size());
    keys.addAll(registryKeys);
    return keys;
  }

  /**
   * {@inheritDoc}
   * <p>
   * This function will create a new modifiable-collection for all this registry's values with a size resolvable as this registry's
   * count of stored-entries as of now, also known as -> {@code this.cache.values().size()}.
   * <p>
   * Modifications performed over the returned collection won't reflect any changes on the original collection.
   *
   * @since 2.3.0
   */
  @Override
  @SuppressWarnings("unchecked")
  public final <C extends Collection<V>> C findAllValues(final Consumer<V> postFetchAction) {
    final C registryValues = (C) this.cache.values();
    final C values = (C) new ArrayList<>(registryValues.size());
    values.addAll(registryValues);
    return values;
  }

  /**
   * {@inheritDoc}
   * <p>
   * This function will create a new modifiable-collection for the filtered-values with a size resolvable as this registry's
   * count of stored-entries as of now, also known as -> {@code this.cache.values().size()}.
   * <p>
   * Elements will be filtered from the original collection then stored into the new one based on elements' (object) properties,
   * depending on how the {@code condition} parameter was configured.
   * <p>
   * Once the filtering finished then the new collection will be returned by this function. Modifications performed over the
   * returned collection won't reflect any changes on the original collection.
   *
   * @since 2.3.0
   */
  @Override
  @SuppressWarnings("unchecked")
  public final <C extends Collection<V>> C filter(final Predicate<V> condition) {
    final C registryValues = (C) this.cache.values();
    final C values = (C) new ArrayList<>(registryValues.size());
    for (final V value : registryValues) {
      if (condition.test(value)) {
        values.add(value);
      }
    }
    return values;
  }

  /**
   * {@inheritDoc}
   * <p>
   * This function only performs a call to {@link Map#clear()}.
   *
   * @since 2.3.0
   */
  @Override
  public void unregisterAll() {
    this.cache.clear();
  }
}
