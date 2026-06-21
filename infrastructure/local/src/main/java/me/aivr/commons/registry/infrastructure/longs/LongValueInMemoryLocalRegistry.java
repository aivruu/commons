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

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import me.aivr.commons.registry.domain.longs.LongValueLocalRegistry;
import me.aivr.commons.registry.infrastructure.AbstractInMemoryLocalRegistry;

import java.util.function.LongConsumer;
import java.util.function.LongPredicate;

/**
 * A registry-type that allows to use {@code long} primitives as values for registry's entries to avoid unboxing/autoboxing
 * operations.
 *
 * @param <K> the type of id this registry uses.
 * @since 2.3.0
 */
public final class LongValueInMemoryLocalRegistry<K> extends AbstractInMemoryLocalRegistry<K, Long, Object2LongMap<K>>
    implements LongValueLocalRegistry<K> {
  /**
   * Creates a new {@link LongValueInMemoryLocalRegistry} with the provided information.
   * <p>
   * This registry will have a pre-defined expected initial-size established by {@link Object2LongOpenHashMap#DEFAULT_INITIAL_SIZE}.
   *
   * @param threadSafe whether the registry must be safe for use between multiple threads.
   * @since 2.3.0
   */
  public LongValueInMemoryLocalRegistry(final boolean threadSafe) {
    this(threadSafe, Object2LongOpenHashMap.DEFAULT_INITIAL_SIZE);
  }

  /**
   * Creates a new {@link LongValueInMemoryLocalRegistry} with the provided information.
   *
   * @param threadSafe whether the registry must be safe for use between multiple threads.
   * @param expectedSize the initial-size that the registry is expected to have.
   * @since 2.3.0
   */
  public LongValueInMemoryLocalRegistry(final boolean threadSafe, final int expectedSize) {
    this(threadSafe ? new Object2LongOpenHashMap<>(expectedSize) : Object2LongMaps.synchronize(new Object2LongOpenHashMap<>(expectedSize)));
  }

  /**
   * Creates a new {@link LongValueInMemoryLocalRegistry} with the provided parameter.
   *
   * @param cache the {@link Object2LongMap} instance to use for cache-handling.
   * @since 2.3.0
   */
  public LongValueInMemoryLocalRegistry(final Object2LongMap<K> cache) {
    super(cache);
  }

  @Override
  public long getLongById(final K id) {
    return super.cache.getLong(id);
  }

  @Override
  public long registerLong(final K id, final long value) {
    final long stored = super.cache.put(id, value);
    return stored == super.cache.defaultReturnValue() ? value : stored;
  }

  @Override
  public LongCollection findAllLongs(final LongConsumer postFetchAction) {
    final LongCollection registryValues = super.cache.values();
    final LongCollection values = new LongArrayList(registryValues.size());
    values.addAll(registryValues);
    return values;
  }

  @Override
  public LongCollection filterLongs(final LongPredicate condition) {
    final LongCollection registryValues = super.cache.values();
    final LongCollection values = new LongArrayList(registryValues.size());
    for (final long value : registryValues) {
      if (condition.test(value)) {
        values.add(value);
      }
    }
    return values;
  }

  @Override
  public long unregisterLong(final K id) {
    return super.cache.removeLong(id);
  }

  @Override
  public int unregisterLongIf(final LongPredicate filter) {
    int count = 0;
    for (final Object2LongMap.Entry<K> entry : super.cache.object2LongEntrySet()) {
      if (filter.test(entry.getLongValue())) {
        super.cache.removeLong(entry.getKey());
        ++count;
      }
    }
    return count;
  }
}
