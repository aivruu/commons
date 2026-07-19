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
package me.aivr.commons.registry.infrastructure.ints;

import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import me.aivr.commons.registry.domain.ints.IntKeyLocalRegistry;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.IntConsumer;
import java.util.function.Predicate;

/**
 * A registry-type that allows to use {@code int} primitives as keys for registry's entries to avoid unboxing/autoboxing
 * operations.
 *
 * @param <V> the type of value this registry handles.
 * @since 3.0.0-rc2
 */
@SuppressWarnings("DataFlowIssue")
public final class IntKeyInMemoryLocalRegistry<V> implements IntKeyLocalRegistry<V> {
  private final Int2ObjectMap<V> cache;

  /**
   * Creates a new {@link IntKeyInMemoryLocalRegistry} with the provided information.
   * <p>
   * This registry will have a pre-defined expected initial-size established by {@link Int2ObjectOpenHashMap#DEFAULT_INITIAL_SIZE}.
   *
   * @param threadSafe whether the registry must be safe for use between multiple threads.
   * @since 3.0.0-rc2
   */
  public IntKeyInMemoryLocalRegistry(final boolean threadSafe) {
    this(threadSafe, Int2ObjectOpenHashMap.DEFAULT_INITIAL_SIZE);
  }

  /**
   * Creates a new {@link IntKeyInMemoryLocalRegistry} with the provided information.
   *
   * @param threadSafe whether the registry must be safe for use between multiple threads.
   * @param expectedSize the initial-size that the registry is expected to have.
   * @since 3.0.0-rc2
   */
  public IntKeyInMemoryLocalRegistry(final boolean threadSafe, final int expectedSize) {
    this(threadSafe ? new Int2ObjectOpenHashMap<>(expectedSize) : Int2ObjectMaps.synchronize(new Int2ObjectOpenHashMap<>(expectedSize)));
  }

  /**
   * Creates a new {@link IntKeyInMemoryLocalRegistry} with the provided parameter.
   *
   * @param cache the {@link Int2ObjectMap} instance to use for cache-handling.
   * @since 3.0.0-rc2
   */
  public IntKeyInMemoryLocalRegistry(final Int2ObjectMap<V> cache) {
    this.cache = cache;
  }

  @Override
  public @Nullable V getByIntId(final int id) {
    return this.cache.get(id);
  }

  @Override
  public V registerInt(final int id, final V value) {
    final V stored = this.cache.put(id, value);
    return stored == this.cache.defaultReturnValue() ? value : stored;
  }

  @Override
  public V registerIntIfAbsent(final int id, final Int2ObjectFunction<V> mappingValueFunc) {
    return this.cache.computeIfAbsent(id, mappingValueFunc);
  }

  @Override
  public @Nullable V registerIntIfPresent(final int id, final BiFunction<? super Integer, ? super V, ? extends V> mappingValueFunc) {
    return this.cache.computeIfPresent(id, mappingValueFunc);
  }

  @Override
  public IntSet findAllIntKeys(final IntConsumer postFetchAction) {
    final IntSet registryKeys = this.cache.keySet();
    final IntSet keys = new IntOpenHashSet(registryKeys.size());
    for (final int key : registryKeys) {
      keys.add(key);
      postFetchAction.accept(key);
    }
    return keys;
  }

  @Override
  public @Nullable V unregisterInt(final int id) {
    return this.cache.remove(id);
  }

  @Override
  public int unregisterIf(final Predicate<V> filter) {
    int count = 0;
    for (final Int2ObjectMap.Entry<V> entry : this.cache.int2ObjectEntrySet()) {
      if (filter.test(entry.getValue())) {
        this.cache.remove(entry.getIntKey());
        ++count;
      }
    }
    return count;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <M extends Map<Integer, V>> M raw() {
    return (M) this.cache;
  }
}
