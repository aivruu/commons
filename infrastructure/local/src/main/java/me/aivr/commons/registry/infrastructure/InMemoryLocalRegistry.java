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

import it.unimi.dsi.fastutil.objects.Object2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import me.aivr.commons.registry.domain.GenericTypeRegistry;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * A registry-implementation that handles data storing/accessing which does not use expirable-entries for the cache.
 *
 * @param <K> the type of id this registry uses.
 * @param <V> the type of value this registry handles.
 * @since 3.0.0-rc2
 */
@SuppressWarnings("DataFlowIssue")
public final class InMemoryLocalRegistry<K, V> implements GenericTypeRegistry<K, V> {
  private final Object2ObjectMap<K, V> cache;

  /**
   * Creates a new {@link InMemoryLocalRegistry} with the provided information.
   * <p>
   * This registry will have a pre-defined expected initial-size established by {@link Object2ObjectOpenHashMap#DEFAULT_INITIAL_SIZE}.
   *
   * @param threadSafe whether the registry must be safe for use between multiple threads.
   * @since 3.0.0-rc2
   */
  public InMemoryLocalRegistry(final boolean threadSafe) {
    this(threadSafe, Object2ObjectOpenHashMap.DEFAULT_INITIAL_SIZE);
  }

  /**
   * Creates a new {@link InMemoryLocalRegistry} with the provided information.
   *
   * @param threadSafe whether the registry must be safe for use between multiple threads.
   * @param expectedSize the initial-size that the registry is expected to have.
   * @since 3.0.0-rc2
   */
  public InMemoryLocalRegistry(final boolean threadSafe, final int expectedSize) {
    this(threadSafe
        ? new Object2ObjectOpenHashMap<>(expectedSize) : Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>(expectedSize)));
  }

  /**
   * Creates a new {@link InMemoryLocalRegistry} with the provided parameter.
   *
   * @param cache the {@link Object2ObjectMap} instance to use for cache-handling.
   * @since 3.0.0-rc2
   */
  public InMemoryLocalRegistry(final Object2ObjectMap<K, V> cache) {
    this.cache = cache;
  }

  @Override
  public @Nullable V findById(final K id) {
    return this.cache.get(id);
  }

  @Override
  public V register(final K id, final V value) {
    final V stored = this.cache.put(id, value);
    return stored == null ? value : stored;
  }

  @Override
  public V registerIfAbsent(final K id, final Object2ObjectFunction<K, V> mappingValueFunc) {
    return this.cache.computeIfAbsent(id, mappingValueFunc);
  }

  @Override
  public @Nullable V registerIfPresent(final K id, final BiFunction<? super K, ? super V, ? extends V> mappingValueFunc) {
    return this.cache.computeIfPresent(id, mappingValueFunc);
  }

  @Override
  public @Nullable V unregister(final K id) {
    return this.cache.remove(id);
  }

  @Override
  public int unregisterIf(final Predicate<V> filter) {
    int count = 0;
    for (final Object2ObjectMap.Entry<K, V> entry : this.cache.object2ObjectEntrySet()) {
      if (filter.test(entry.getValue())) {
        this.cache.remove(entry.getKey());
        ++count;
      }
    }
    return count;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <M extends Map<K, V>> M raw() {
    return (M) this.cache;
  }
}
