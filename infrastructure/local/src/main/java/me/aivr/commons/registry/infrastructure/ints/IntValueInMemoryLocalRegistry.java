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

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import me.aivr.commons.registry.domain.ints.IntValueLocalRegistry;
import me.aivr.commons.registry.infrastructure.AbstractInMemoryLocalRegistry;
import org.jspecify.annotations.NullMarked;

/**
 * A registry-type that allows to use {@code int} primitives as values for registry's entries to avoid unboxing/autoboxing
 * operations.
 *
 * @param <K> the type of id this registry uses.
 * @since 2.3.0
 */
@NullMarked
public final class IntValueInMemoryLocalRegistry<K> extends AbstractInMemoryLocalRegistry<K, Integer, Object2IntMap<K>>
    implements IntValueLocalRegistry<K> {
  /**
   * Creates a new {@link IntValueInMemoryLocalRegistry} with the provided information.
   * <p>
   * This registry will have a pre-defined expected initial-size established by {@link Object2IntOpenHashMap#DEFAULT_INITIAL_SIZE}.
   *
   * @param threadSafe whether the registry must be safe for use between multiple threads.
   * @since 2.3.0
   */
  public IntValueInMemoryLocalRegistry(final boolean threadSafe) {
    this(threadSafe, Object2IntOpenHashMap.DEFAULT_INITIAL_SIZE);
  }

  /**
   * Creates a new {@link IntValueInMemoryLocalRegistry} with the provided information.
   *
   * @param threadSafe whether the registry must be safe for use between multiple threads.
   * @param expectedSize the initial-size that the registry is expected to have.
   * @since 2.3.0
   */
  public IntValueInMemoryLocalRegistry(final boolean threadSafe, final int expectedSize) {
    this(threadSafe ? new Object2IntOpenHashMap<>(expectedSize) : Object2IntMaps.synchronize(new Object2IntOpenHashMap<>(expectedSize)));
  }

  /**
   * Creates a new {@link IntValueInMemoryLocalRegistry} with the provided parameter.
   *
   * @param cache the {@link Object2IntMap} instance to use for cache-handling.
   * @since 2.3.0
   */
  public IntValueInMemoryLocalRegistry(final Object2IntMap<K> cache) {
    super(cache);
  }

  @Override
  public int getIntById(final K id) {
    return super.cache.getInt(id);
  }

  @Override
  public int registerInt(final K id, final int value) {
    final int stored = super.cache.put(id, value);
    return stored == super.cache.defaultReturnValue() ? value : stored;
  }

  @Override
  public IntCollection findAllInts(final IntConsumer postFetchAction) {
    final IntCollection registryValues = super.cache.values();
    final IntCollection values = new IntArrayList(registryValues.size());
    values.addAll(registryValues);
    return values;
  }

  @Override
  public IntCollection filterInts(final java.util.function.IntPredicate condition) {
    final IntCollection registryValues = super.cache.values();
    final IntCollection values = new IntArrayList(registryValues.size());
    for (final int value : registryValues) {
      if (condition.test(value)) {
        values.add(value);
      }
    }
    return values;
  }

  @Override
  public int unregisterInt(final K id) {
    return super.cache.removeInt(id);
  }

  @Override
  public int unregisterIntIf(final IntPredicate filter) {
    int count = 0;
    for (final Object2IntMap.Entry<K> entry : super.cache.object2IntEntrySet()) {
      if (filter.test(entry.getIntValue())) {
        super.cache.removeInt(entry.getKey());
        ++count;
      }
    }
    return count;
  }
}
