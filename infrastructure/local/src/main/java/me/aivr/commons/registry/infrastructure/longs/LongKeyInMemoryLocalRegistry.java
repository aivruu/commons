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
package me.aivr.commons.registry.infrastructure.longs;

import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import me.aivr.commons.registry.domain.longs.LongKeyLocalRegistry;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.LongConsumer;
import java.util.function.Predicate;

/**
 * A registry-type that allows to use {@code long} primitives as keys for registry's entries to avoid unboxing/autoboxing
 * operations.
 *
 * @param <V> the type of value this registry handles.
 * @since 3.0.0-rc2
 */
@SuppressWarnings("DataFlowIssue")
public final class LongKeyInMemoryLocalRegistry<V> implements LongKeyLocalRegistry<V> {
  private final Long2ObjectMap<V> cache;

  /**
   * Creates a new {@link LongKeyInMemoryLocalRegistry} with the provided information.
   * <p>
   * This registry will have a pre-defined expected initial-size established by {@link Long2ObjectOpenHashMap#DEFAULT_INITIAL_SIZE}.
   *
   * @param threadSafe whether the registry must be safe for use between multiple threads.
   * @since 3.0.0-rc2
   */
  public LongKeyInMemoryLocalRegistry(final boolean threadSafe) {
    this(threadSafe, Long2ObjectOpenHashMap.DEFAULT_INITIAL_SIZE);
  }

  /**
   * Creates a new {@link LongKeyInMemoryLocalRegistry} with the provided information.
   *
   * @param threadSafe whether the registry must be safe for use between multiple threads.
   * @param expectedSize the initial-size that the registry is expected to have.
   * @since 3.0.0-rc2
   */
  public LongKeyInMemoryLocalRegistry(final boolean threadSafe, final int expectedSize) {
    this(threadSafe ? new Long2ObjectOpenHashMap<>(expectedSize) : Long2ObjectMaps.synchronize(new Long2ObjectOpenHashMap<>(expectedSize)));
  }

  /**
   * Creates a new {@link LongKeyInMemoryLocalRegistry} with the provided parameter.
   *
   * @param cache the {@link Long2ObjectMap} instance to use for cache-handling.
   * @since 3.0.0-rc2
   */
  public LongKeyInMemoryLocalRegistry(final Long2ObjectMap<V> cache) {
    this.cache = cache;
  }

  @Override
  public @Nullable V getByLongId(final long id) {
    return this.cache.get(id);
  }

  @Override
  public V registerLong(final long id, final V value) {
    final V stored = this.cache.put(id, value);
    return stored == this.cache.defaultReturnValue() ? value : stored;
  }

  @Override
  public V registerLongIfAbsent(final long id, final Long2ObjectFunction<V> mappingValueFunc) {
    return this.cache.computeIfAbsent(id, mappingValueFunc);
  }

  @Override
  public @Nullable V registerLongIfPresent(final long id, final BiFunction<? super Long, ? super V, ? extends V> mappingValueFunc) {
    return this.cache.computeIfPresent(id, mappingValueFunc);
  }

  @Override
  public LongSet findAllLongKeys(final LongConsumer postFetchAction) {
    final LongSet registryKeys = this.cache.keySet();
    final LongSet keys = new LongOpenHashSet(registryKeys.size());
    for (final long key : registryKeys) {
      keys.add(key);
      postFetchAction.accept(key);
    }
    return keys;
  }

  @Override
  public @Nullable V unregisterLong(final long id) {
    return this.cache.remove(id);
  }

  @Override
  public int unregisterIf(final Predicate<V> filter) {
    int count = 0;
    for (final Long2ObjectMap.Entry<V> entry : this.cache.long2ObjectEntrySet()) {
      if (filter.test(entry.getValue())) {
        this.cache.remove(entry.getLongKey());
        ++count;
      }
    }
    return count;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <M extends Map<Long, V>> M raw() {
    return (M) this.cache;
  }
}
