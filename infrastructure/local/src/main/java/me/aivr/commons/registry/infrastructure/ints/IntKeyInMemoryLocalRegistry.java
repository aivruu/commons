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

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import me.aivr.commons.registry.domain.ints.IntKeyLocalRegistry;
import me.aivr.commons.registry.infrastructure.AbstractInMemoryLocalRegistry;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A registry-type that allows to use {@code int} primitives as keys for registry's entries to avoid unboxing/autoboxing
 * operations.
 *
 * @param <V> the type of value this registry handles.
 * @since 2.3.0
 */
@NullMarked
@SuppressWarnings("DataFlowIssue")
public final class IntKeyInMemoryLocalRegistry<V> extends AbstractInMemoryLocalRegistry<Integer, V, Int2ObjectMap<V>>
    implements IntKeyLocalRegistry<V> {
  /**
   * Creates a new {@link IntKeyInMemoryLocalRegistry} with the provided information.
   * <p>
   * This registry will have a pre-defined expected initial-size established by {@link Int2ObjectOpenHashMap#DEFAULT_INITIAL_SIZE}.
   *
   * @param threadSafe whether the registry must be safe for use between multiple threads.
   * @since 2.3.0
   */
  public IntKeyInMemoryLocalRegistry(final boolean threadSafe) {
    this(threadSafe, Int2ObjectOpenHashMap.DEFAULT_INITIAL_SIZE);
  }

  /**
   * Creates a new {@link IntKeyInMemoryLocalRegistry} with the provided information.
   *
   * @param threadSafe whether the registry must be safe for use between multiple threads.
   * @param expectedSize the initial-size that the registry is expected to have.
   * @since 2.3.0
   */
  public IntKeyInMemoryLocalRegistry(final boolean threadSafe, final int expectedSize) {
    this(threadSafe ? new Int2ObjectOpenHashMap<>(expectedSize) : Int2ObjectMaps.synchronize(new Int2ObjectOpenHashMap<>(expectedSize)));
  }

  /**
   * Creates a new {@link IntKeyInMemoryLocalRegistry} with the provided parameter.
   *
   * @param cache the {@link Int2ObjectMap} instance to use for cache-handling.
   * @since 2.3.0
   */
  public IntKeyInMemoryLocalRegistry(final Int2ObjectMap<V> cache) {
    super(cache);
  }

  @Override
  public @Nullable V getByIntId(final int id) {
    return super.cache.get(id);
  }

  @Override
  public V registerInt(final int id, final V value) {
    final V stored = super.cache.put(id, value);
    return stored == super.cache.defaultReturnValue() ? value : stored;
  }

  @Override
  public IntSet findAllIntKeys(final IntConsumer postFetchAction) {
    final IntSet registryKeys = super.cache.keySet();
    final IntSet keys = new IntOpenHashSet(registryKeys.size());
    keys.addAll(registryKeys);
    return keys;
  }

  @Override
  public @Nullable V unregisterInt(final int id) {
    return super.cache.remove(id);
  }

  @Override
  public int unregisterIf(final Predicate<V> filter) {
    int count = 0;
    for (final Int2ObjectMap.Entry<V> entry : super.cache.int2ObjectEntrySet()) {
      if (filter.test(entry.getValue())) {
        super.cache.remove(entry.getIntKey());
        ++count;
      }
    }
    return count;
  }
}
