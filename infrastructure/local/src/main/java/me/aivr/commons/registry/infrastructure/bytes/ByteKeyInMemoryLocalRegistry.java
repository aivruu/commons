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
package me.aivr.commons.registry.infrastructure.bytes;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMaps;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.bytes.ByteConsumer;
import it.unimi.dsi.fastutil.bytes.ByteOpenHashSet;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import me.aivr.commons.registry.domain.bytes.ByteKeyLocalRegistry;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.function.Predicate;

/**
 * A registry-type that allows to use {@code byte} primitives as keys for registry's entries to avoid unboxing/autoboxing
 * operations.
 *
 * @param <V> the type of value this registry handles.
 * @since 3.0.0-rc2
 */
@SuppressWarnings("DataFlowIssue")
public final class ByteKeyInMemoryLocalRegistry<V> implements ByteKeyLocalRegistry<V> {
  private final Byte2ObjectMap<V> cache;

  /**
   * Creates a new {@link ByteKeyInMemoryLocalRegistry} with the provided information.
   * <p>
   * This registry will have a pre-defined expected initial-size established by {@link Byte2ObjectOpenHashMap#DEFAULT_INITIAL_SIZE}.
   *
   * @param threadSafe whether the registry must be safe for use between multiple threads.
   * @since 3.0.0-rc2
   */
  public ByteKeyInMemoryLocalRegistry(final boolean threadSafe) {
    this(threadSafe, Byte2ObjectOpenHashMap.DEFAULT_INITIAL_SIZE);
  }

  /**
   * Creates a new {@link ByteKeyInMemoryLocalRegistry} with the provided information.
   *
   * @param threadSafe whether the registry must be safe for use between multiple threads.
   * @param expectedSize the initial-size that the registry is expected to have.
   * @since 3.0.0-rc2
   */
  public ByteKeyInMemoryLocalRegistry(final boolean threadSafe, final int expectedSize) {
    this(threadSafe ? new Byte2ObjectOpenHashMap<>(expectedSize) : Byte2ObjectMaps.synchronize(new Byte2ObjectOpenHashMap<>(expectedSize)));
  }

  /**
   * Creates a new {@link ByteKeyInMemoryLocalRegistry} with the provided parameter.
   *
   * @param cache the {@link Byte2ObjectMap} instance to use for cache-handling.
   * @since 3.0.0-rc2
   */
  public ByteKeyInMemoryLocalRegistry(final Byte2ObjectMap<V> cache) {
    this.cache = cache;
  }

  @Override
  public @Nullable V getByByteId(final byte id) {
    return this.cache.get(id);
  }

  @Override
  public V registerByte(final byte id, final V value) {
    final V stored = this.cache.put(id, value);
    return stored == this.cache.defaultReturnValue() ? value : stored;
  }

  @Override
  public ByteSet findAllByteKeys(final ByteConsumer postFetchAction) {
    final ByteSet registryKeys = this.cache.keySet();
    final ByteSet keys = new ByteOpenHashSet(registryKeys.size());
    for (final byte key : registryKeys) {
      keys.add(key);
      postFetchAction.accept(key);
    }
    return keys;
  }

  @Override
  public @Nullable V unregisterByte(final byte id) {
    return this.cache.remove(id);
  }

  @Override
  public int unregisterIf(final Predicate<V> filter) {
    int count = 0;
    for (final Byte2ObjectMap.Entry<V> entry : this.cache.byte2ObjectEntrySet()) {
      if (filter.test(entry.getValue())) {
        this.cache.remove(entry.getByteKey());
        ++count;
      }
    }
    return count;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <M extends Map<Byte, V>> M raw() {
    return (M) this.cache;
  }
}
